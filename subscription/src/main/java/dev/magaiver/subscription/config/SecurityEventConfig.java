package dev.magaiver.subscription.config;

import dev.magaiver.token.config.SecurityTokenConfig;
import dev.magaiver.token.property.JwtConfiguration;
import dev.magaiver.token.security.filter.AuthorizationFilter;
import dev.magaiver.token.security.jwt.TokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Magaiver Santos
 */

@Configuration
@Order
public class SecurityEventConfig extends SecurityTokenConfig {
    private final JwtConfiguration jwtConfiguration;
    private final TokenProvider tokenProvider;

    public SecurityEventConfig(JwtConfiguration jwtConfiguration, TokenProvider tokenProvider) {
        this.jwtConfiguration = jwtConfiguration;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new AuthorizationFilter(jwtConfiguration, tokenProvider), UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }
}
