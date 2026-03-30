package com.taskify.taskifyApi.application.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.taskify.taskifyApi.infrastructure.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final AppProperties appProperties;

    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(appProperties.getSecurity().getJwtKeySecret());

        String username;
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oAuth2User) {
            username = oAuth2User.getAttribute("email");
            if (username == null) {
                username = oAuth2User.getAttribute("login");
            }
        } else if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            username = Objects.toString(principal, "anonymous");
        }

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return JWT.create()
                .withIssuer(appProperties.getSecurity().getJwtIssuerName())
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + appProperties.getSecurity().getJwtExpirationMs()))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(appProperties.getSecurity().getJwtKeySecret());

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(appProperties.getSecurity().getJwtIssuerName())
                    .build();

            return verifier.verify(token);

        }catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token invalid, not Authorized.");
        }
    }

    public String getUsernameFromToken(DecodedJWT decodedToken) {
        return decodedToken.getSubject();
    }

    public Claim getClaimFromToken(DecodedJWT decodedToken, String claimName) {
        return decodedToken.getClaim(claimName);
    }

    public Map<String, Claim> getAllClaimsFromToken(DecodedJWT decodedToken) {
        return decodedToken.getClaims();
    }
}

