package com.springboot.blocket.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {
    private static final String secret = "super-secret-key";

    public static String createToken(String subject, String name) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(subject)
                .withClaim("name", name)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(algorithm);
    }

    public static boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public static String getSubjectFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
