//package com.example.branflu.configuration;
//
//import com.example.branflu.security.CustomUserDetailsService;
//import com.example.branflu.security.JWTAuthenticationEntryPoint;
//import com.example.branflu.security.JWTAuthenticationFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//@RequiredArgsConstructor
//public class SecurityConfiguration {
//
//    private final JWTAuthenticationFilter    jwtAuthenticationFilter;
//    private final JWTAuthenticationEntryPoint authEntryPoint;
//    private final CustomUserDetailsService   userDetailsService;
//
//    // 1) PUBLIC Chain: only for registration & swagger, no JWT filter
//    @Bean
//    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher(
//                        "/influencer/register",
//                        "/business/register",
//                        "/api/v1/auth/**",
//                        "/v3/api-docs/**",
//                        "/swagger-ui/**",
//                        "/swagger-resources/**",
//                        "/webjars/**"
//                )
//                .cors(cors -> cors.configurationSource(corsConfig()))
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                );
//        return http.build();
//    }
//
//    // 2) PROTECTED Chain: everything else goes here
//    @Bean
//    public SecurityFilterChain protectedFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/**")
//                .cors(cors -> cors.configurationSource(corsConfig()))
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint(authEntryPoint)
//                )
//                .sessionManagement(sm -> sm
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authenticationProvider(authenticationProvider());
//
//        // add the JWT filter
//        http.addFilterBefore(
//                jwtAuthenticationFilter,
//                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
//        );
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        var dao = new DaoAuthenticationProvider();
//        dao.setUserDetailsService(userDetailsService);
//        dao.setPasswordEncoder(passwordEncoder());
//        return dao;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration config
//    ) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    // Shared CORS config
//    private CorsConfigurationSource corsConfig() {
//        var cfg = new CorsConfiguration()
//                .applyPermitDefaultValues();  // allows GET, POST, HEAD by default
//        cfg.setAllowedOrigins(List.of("*"));
//        cfg.setAllowedMethods(
//                List.of(
//                        HttpMethod.GET.name(),
//                        HttpMethod.POST.name(),
//                        HttpMethod.PUT.name(),
//                        HttpMethod.DELETE.name(),
//                        HttpMethod.OPTIONS.name()
//                )
//        );
//        cfg.setAllowedHeaders(List.of("*"));
//        cfg.setAllowCredentials(true);
//        var src = new UrlBasedCorsConfigurationSource();
//        src.registerCorsConfiguration("/**", cfg);
//        return src;
//    }
//}
