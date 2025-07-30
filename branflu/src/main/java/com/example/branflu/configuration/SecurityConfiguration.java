package com.example.branflu.configuration;

import com.example.branflu.security.JWTAuthenticationEntryPoint;
import com.example.branflu.security.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfiguration(
            JWTAuthenticationFilter jwtAuthenticationFilter,
            JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/influencer/register",
                                "/api/business/register",
                                "/login/influencer",
                                "/login/buisness",
                                "/api/youtube/auth",
                                "/api/youtube/callback",
                                "/api/facebook/login",
                                "/api/facebook/callback",
                                "/api/instagram/callback",
                                "/api/instagram/login",
                                "/api/instagram/webhook"

                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
