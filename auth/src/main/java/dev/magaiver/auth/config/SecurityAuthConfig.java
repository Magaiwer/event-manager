package dev.magaiver.auth.config;

import dev.magaiver.auth.security.filter.AuthenticationFilter;
import dev.magaiver.auth.service.CustomUserDetailServiceImpl;
import dev.magaiver.token.config.SecurityTokenConfig;
import dev.magaiver.token.property.JwtConfiguration;
import dev.magaiver.token.security.filter.AuthorizationFilter;
import dev.magaiver.token.security.jwt.TokenProvider;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Order(1)
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
        super.configure(http);
    }

}
