package com.taskify.taskifyApi.infrastructure.input.config.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.taskify.taskifyApi.application.utils.JwtUtils;
import com.taskify.taskifyApi.infrastructure.input.model.request.ClientType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RequiredArgsConstructor
public class JwtTokenValidator extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String jwtToken = extractJWT(request);

        if (jwtToken != null) {
            DecodedJWT decodedJWT = jwtUtils.verifyToken(jwtToken);

            String username = jwtUtils.getUsernameFromToken(decodedJWT);
            String stringAuthorities = jwtUtils.getClaimFromToken(decodedJWT, "authorities").asString();

            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }

    public String extractJWT(HttpServletRequest request) {
        String headerValue = request.getHeader("X-Client-Type");
        ClientType clientType = ClientType.fromHeader(headerValue);
        String jwtToken = null;

        switch (clientType) {
            case DESKTOP -> {
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    jwtToken = authHeader.substring(7);
                }
            }
            case WEB -> {
                if (request.getCookies() != null) {
                    jwtToken = Arrays.stream(request.getCookies())
                            .filter(cookie -> "jwt".equals(cookie.getName()))
                            .map(Cookie::getValue)
                            .findFirst()
                            .orElse(null);
                }
            }
        }

        return jwtToken;
    }
}