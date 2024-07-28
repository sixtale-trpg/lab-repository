package org.infinity.sixtalebackend.domain.member.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.member.exception.UnAuthorizedException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JWTUtil {

    private String salt;
    private long accessTokenExpireTime;
    private long refreshTokenExpireTime;

    public String createAccessToken(String userID) {
        return create(userID, "access-token", accessTokenExpireTime);
    }

    public String createRefreshToken(String userID) {
        return create(userID, "refresh-token", refreshTokenExpireTime);
    }

    /**
     * Token 발급
     *    key : Claim에 세팅될 key 값
     *    value : Claim에 세팅될 data 값
     *    subject : payload에 sub의 value로 들어갈 subject 값
     *    expire : 토큰 유효기간 설정을 위한 값
     *    jwt 토큰의 구성 : header + payload + signature
     */
    private String create(String userID, String subject, long expireTime) {
        Claims claims = Jwts.claims()
                .setSubject(subject) // 토큰 제목 설정
                .setIssuedAt(new Date()) // 생성일 설정
                .setExpiration(new Date(System.currentTimeMillis() + expireTime));

        claims.put("userID", userID);

        String jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT").setClaims(claims) // Header 설정 : 토큰 타입, 해쉬 알고리즘 정보 세팅
                .signWith(SignatureAlgorithm.HS256, this.generateKey()) // Signature 설정 : secret key 활용한 암호화
                .compact(); // 직렬화 처리

        log.info("jwt : {}", jwt);
        return jwt;
    }

    /**
     * signature 설정에 들어갈 key 생성
     */
    private byte[] generateKey() {
        byte[] key = null;
        try {
            key = salt.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            if (log.isInfoEnabled()) {
                e.printStackTrace();
            } else {
                log.error("Making JWT Key Error ::: {}", e.getMessage());
            }
        }
        return key;
    }

    /**
     * 전달 받은 토큰이 제대로 생성된 것인지 확인
     * 문제 발생 시 UnauthorizedException 발생
     */
    public boolean checkToken(String token) {
        System.out.println("token: " + token);
        try {
//			Json Web Signature? 서버에서 인증을 근거로 인증 정보를 서버의 private key 서명 한것을 토큰화 한것
//			setSigningKey : JWS 서명 검증을 위한  secret key 세팅
//			parseClaimsJws : 파싱하여 원본 jws 만들기
            Jws<Claims> claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(token);
//			Claims 는 Map 구현체 형태
            log.debug("claims: {}", claims);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public String getUserId(String authorization) {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(authorization);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnAuthorizedException();
        }
        Map<String, Object> value = claims.getBody();
        log.info("value : {}", value);
        return (String) value.get("userId");
    }
}
