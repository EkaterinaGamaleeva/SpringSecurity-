package com.SpringSecurityTest.SpringSecurityTest.sucurity;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;


@Component
public class JwtUtil {
    @Value("${jwt_secret}")
    private String secret;


    public String generateToken(ConsumerDetails consumerDetails) {
        Date date = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        // Логика генерации JWT
        return JWT.create().withSubject("Consumer details")
                .withClaim("username", consumerDetails.getUsername())
                .withClaim("password", consumerDetails.getPassword())
                .withClaim("name", consumerDetails.getConsumer().getName())
                .withClaim("email", consumerDetails.getConsumer().getEmail())
                .withClaim("id", consumerDetails.getConsumer().getId())
                .withIssuedAt(new Date())
                .withIssuer("EkaterinaGamaleeva")
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(secret));

    }

    public String validateToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).withSubject("Consumer details").withIssuer("EkaterinaGamaleeva")
                .build();
        DecodedJWT jwt = jwtVerifier.verify(token);
       return jwt.getClaim("username").asString();
    }
}
