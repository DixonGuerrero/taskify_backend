package com.taskify.taskifyApi.application.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${security.jwt.key.secret}")
    private String secretKey;

    @Value("${security.jwt.issuer.name}")
    private String issuer;


    public String createToken(Authentication authentication) {

        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);

        String username = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return JWT.create()
                .withIssuer(this.issuer)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(this.secretKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.issuer)
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

