package com.example.branflu.service.implementation;

import com.example.branflu.store.OtpEntry;
import com.example.branflu.store.OtpStore;
import com.example.branflu.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final OtpStore otpStore;

    @Value("${app.otp.ttl-seconds:300}")
    private long otpTtlSeconds; // 5 minutes default

    @Value("${app.otp.hmac-secret}")
    private String hmacSecret; // set in application.properties / env

    @Value("${app.otp.debug:false}")
    private boolean debug; // whether to log OTPs (dev only)

    // simple per-email rate limit: max sends per window
    @Value("${app.otp.send-limit:5}")
    private int maxSends;

    @Value("${app.otp.send-window-seconds:3600}")
    private int sendWindowSeconds;

    // in-memory counters for rate-limit (replace with Redis counters in prod)
    private final java.util.Map<String, java.util.concurrent.atomic.AtomicInteger> sendCounts = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<String, Instant> sendWindowStart = new java.util.concurrent.ConcurrentHashMap<>();

    private Mac mac;

    @PostConstruct
    public void init() {
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("Failed to initialize HMAC", e);
        }
    }

    @Override
    public synchronized void generateAndSendOtp(String email, String ip) {
        String key = email.toLowerCase();

        // rate limiting per email - simple sliding window
        Instant windowStart = sendWindowStart.getOrDefault(key, Instant.now());
        if (Duration.between(windowStart, Instant.now()).getSeconds() > sendWindowSeconds) {
            sendWindowStart.put(key, Instant.now());
            sendCounts.put(key, new java.util.concurrent.atomic.AtomicInteger(0));
        }
        int sent = sendCounts.computeIfAbsent(key, k -> new java.util.concurrent.atomic.AtomicInteger(0)).incrementAndGet();
        if (sent > maxSends) {
            throw new IllegalStateException("Too many OTP requests. Try again later.");
        }

        // generate otp
        String otp = String.format("%06d", new Random().nextInt(1_000_000));
        String otpHash = hmac(otp);

        // persist hashed OTP with TTL
        otpStore.put(key, otpHash, otpTtlSeconds);

        // send email
        sendOtpEmail(email, otp);

        if (debug) {
            // debug: log the OTP (ONLY ENABLE FOR DEVELOPMENT)
            System.out.printf("DEBUG OTP for %s = %s (expires in %ds)%n", email, otp, otpTtlSeconds);
        }
    }

    @Override
    public synchronized boolean verifyOtp(String email, String otp) {
        String key = email.toLowerCase();
        var opt = otpStore.get(key);
        if (opt.isEmpty()) return false;
        OtpEntry entry = opt.get();
        if (entry.isUsed()) return false;

        String providedHash = hmac(otp);
        if (!providedHash.equals(entry.getOtpHash())) {
            return false;
        }

        // mark used (single-use) and remove
        otpStore.remove(key);
        return true;
    }

    private String hmac(String s) {
        try {
            Mac localMac = (Mac) mac.clone(); // Mac is not thread-safe
            byte[] res = localMac.doFinal(s.getBytes(StandardCharsets.UTF_8));
            return java.util.Base64.getEncoder().encodeToString(res);
        } catch (CloneNotSupportedException e) {
            // fallback - create new Mac (less efficient)
            try {
                Mac m2 = Mac.getInstance("HmacSHA256");
                m2.init(new SecretKeySpec(hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                return java.util.Base64.getEncoder().encodeToString(m2.doFinal(s.getBytes(StandardCharsets.UTF_8)));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void sendOtpEmail(String toEmail, String otp) {
        Context context = new Context();
        context.setVariable("otp", otp);

        String html = templateEngine.process("verificationOtp", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Your Branflu verification code");
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new RuntimeException("Failed to send OTP email", ex);
        }
    }
}
