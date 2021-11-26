package dev.magaiver.user.config;

import dev.magaiver.token.config.SecurityTokenConfig;
import dev.magaiver.token.property.JwtConfiguration;
import dev.magaiver.token.security.filter.AuthorizationFilter;
import dev.magaiver.token.security.jwt.TokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Magaiver Santos
 */
@Configuration
public class SecurityConfig extends SecurityTokenConfig {
    private final JwtConfiguration jwtConfiguration;
    private final TokenProvider tokenProvider;

    public SecurityConfig(JwtConfiguration jwtConfiguration, TokenProvider tokenProvider) {
        this.jwtConfiguration = jwtConfiguration;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new AuthorizationFilter(jwtConfiguration, tokenProvider), UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }
}
