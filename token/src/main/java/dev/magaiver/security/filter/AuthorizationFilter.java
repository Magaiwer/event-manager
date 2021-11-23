package dev.magaiver.security.filter;

import dev.magaiver.domain.model.User;
import dev.magaiver.security.UserDetail;
import dev.magaiver.security.jwt.TokenProvider;
import dev.magaiver.property.JwtConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Magaiver Santos
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtConfiguration jwtConfiguration;
    private final TokenProvider tokenProvider;

    @Override
    @NonNull
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(jwtConfiguration.getHeaderName());

        if (header == null || !header.startsWith(jwtConfiguration.getHeaderValue())) {
            chain.doFilter(request, response);
            return;
        }

        String decryptedToken = getToken(header);
        tokenProvider.validateTokenSignature(decryptedToken);


        if (!tokenProvider.validateToken(decryptedToken)) {
            log.error("Token expired");
            chain.doFilter(request, response);
            return;
        }

        log.info("Token is Valid ");
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(decryptedToken));
        chain.doFilter(request, response);
    }

    private String getToken(String header) {
        String token = header.replace(jwtConfiguration.getHeaderValue(), "").trim();
        return jwtConfiguration.getType().equals("signed") ? token : tokenProvider.decryptToken(token);
    }

    public Authentication getAuthentication(String token) {
        String username = tokenProvider.getUsernameFromToken(token);
        UserDetail userDetails = UserDetail
                .builder()
                .username(username)
                .role(String.join(",", tokenProvider.getStringListAuthorities(token)))
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, null, tokenProvider.getAuthoritiesFromToken(token));
    }


}
