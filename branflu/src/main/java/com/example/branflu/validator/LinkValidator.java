package com.example.branflu.validator;

import com.example.branflu.entity.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;
import java.util.regex.Pattern;

@Component
public class LinkValidator {

    private static final Logger log = LoggerFactory.getLogger(LinkValidator.class);

    private static final Pattern LINK_PATTERN = Pattern.compile(
            "^(https?://)?(www\\.)?" +
                    "(" +
                    "youtube\\.com|" +
                    "youtu\\.be|" +
                    "x\\.com|" +
                    "twitter\\.com|" +
                    "instagram\\.com|" +
                    "facebook\\.com|" +
                    "snapchat\\.com|" +
                    "pinterest\\.com|" +
                    "linkedin\\.com|" +
                    "reddit\\.com|" +
                    "twitch\\.tv|" +
                    "t\\.me|" +
                    "wa\\.me|" +
                    "chat\\.whatsapp\\.com|" +
                    "threads\\.net|" +
                    "clubhouse\\.com|" +
                    "discord\\.com|" +
                    "discord\\.gg|" +
                    "medium\\.com|" +
                    "substack\\.com|" +
                    "blogspot\\.com|" +
                    "wordpress\\.com" +
                    ")" +
                    "/[A-Za-z0-9._%+\\-/?=&]*$",
            Pattern.CASE_INSENSITIVE
    );

    public void isValidLink(Link link, Supplier<? extends RuntimeException> throwableSupplier) {
        if (link == null || link.getUrl() == null || link.getUrl().trim().isEmpty()) {
            log.warn("LinkValidator >> Link or URL is null or empty");
            throw throwableSupplier.get();
        }

        String url = link.getUrl().trim();
        log.info("LinkValidator >> isValidLink -> {}", url);

        if (!LINK_PATTERN.matcher(url).matches()) {
            log.warn("LinkValidator >> Invalid format -> {}", url);
            throw throwableSupplier.get();
        }
    }
}