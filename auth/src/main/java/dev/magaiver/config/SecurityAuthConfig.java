package dev.magaiver.config;


import dev.magaiver.property.JwtConfiguration;
import dev.magaiver.security.filter.AuthenticationFilter;
import dev.magaiver.security.filter.AuthorizationFilter;
import dev.magaiver.security.jwt.TokenProvider;
import dev.magaiver.service.CustomUserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;


@EnableWebSecurity
public class SecurityAuthConfig extends SecurityTokenConfig {
    private final CustomUserDetailServiceImpl customUserDetailService;
    private final JwtConfiguration jwtConfiguration;
    private final TokenProvider tokenProvider;

    public SecurityAuthConfig(TokenProvider tokenProvider, CustomUserDetailServiceImpl customUserDetailService, JwtConfiguration jwtConfiguration) {
        this.customUserDetailService = customUserDetailService;
        this.jwtConfiguration = jwtConfiguration;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilter(new AuthenticationFilter(authenticationManager(), tokenProvider, jwtConfiguration));
        http.addFilterAfter(new AuthorizationFilter(jwtConfiguration, tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
        return corsConfiguration;
    }
}
