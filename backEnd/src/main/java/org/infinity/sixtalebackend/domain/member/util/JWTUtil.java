package org.infinity.sixtalebackend.domain.member.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 86400000; // 1일

    public static String generateToken(String userEmail) {
        log.info("userEmail" + userEmail);
        try {
            JwtBuilder builder = Jwts.builder().setSubject(userEmail);
            log.info("builder complete");
            JwtBuilder builder2 = builder.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
            log.info("builder2 complete");
            JwtBuilder builder3 = builder2.signWith(SignatureAlgorithm.HS512, SECRET_KEY);
            log.info("builder3 complete");
            String str = builder3.compact();
            log.info("compact complete");
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }

        return Jwts.builder()
                .setSubject(userEmail)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String getUserIDFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
