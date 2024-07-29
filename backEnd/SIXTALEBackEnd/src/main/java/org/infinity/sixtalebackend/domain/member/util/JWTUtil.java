package org.infinity.sixtalebackend.domain.member.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JWTUtil {
    private static final String SECRET_KEY = "mySecretKey";
    private static final long EXPIRATION_TIME = 86400000; // 1일

    public static String generateToken(String userEmail) {
        log.info("userEmail" + userEmail);

        JwtBuilder builder = Jwts.builder().setSubject(userEmail);
        log.info("builder complete");
        JwtBuilder builder2 = builder.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        log.info("builder2 complete");
        JwtBuilder builder3 = builder2.signWith(SignatureAlgorithm.HS512, SECRET_KEY);
        log.info("builder3 complete");
        String str = builder3.compact();
        log.info("compact complete");

        return Jwts.builder()
                .setSubject(userEmail)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
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
