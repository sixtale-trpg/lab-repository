package org.infinity.sixtalebackend.domain.member.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.service.AuthServiceImpl;
import org.infinity.sixtalebackend.domain.member.service.MemberSerivceImpl;
import org.infinity.sixtalebackend.domain.member.util.JWTUtil;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@Slf4j
@RestController
@RequestMapping("/members")
@AllArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final JWTUtil jwtUtil;
    private static final String SECRET_KEY = "mySecretKey";
    private final MemberSerivceImpl memberSerivceImpl;

    public String getProtectedResource(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return "Unauthorized access";
        }

        String token = authorizationHeader.substring(7);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // JWT가 유효한 경우, 사용자 정보 추출
            String userId = claims.getSubject();
            return "Protected resource accessed by user: " + userId;
        } catch (Exception e) {
            return "Invalid token";
        }
    }

    /**
     * 로그인
     */
    @GetMapping("/auth/login/{registrationID}")
    public ResponseEntity<?> login(@RequestParam String code, @PathVariable String registrationID,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            Member member = authService.socialLogin(code, registrationID);
            String accessToken = jwtUtil.generateToken(member.getEmail());
            authService.saveAccessToken(member, accessToken);
            
            // HTTP-Only 및 Secure 속성 사용해서 보안 강화해야함
            Cookie cookie = new Cookie("access-token", accessToken);
            cookie.setHttpOnly(false);
            cookie.setSecure(false); // HTTPS를 사용하는 경우에만 설정
            cookie.setPath("/");
            cookie.setMaxAge(86400); // 1일

            response.addCookie(cookie);
            response.sendRedirect("http://localhost:8083");

            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, accessToken), HttpStatus.OK);
        } catch(Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        try {
            Cookie cookie = new Cookie("access-token", "");
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // HTTPS를 사용하는 경우에만 설정
            cookie.setPath("/");
            cookie.setMaxAge(0); // 쿠키 삭제
            response.addCookie(cookie);

            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGOUT_SUCCESS), HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원탈퇴
     */
    @PatchMapping("/withdraw")
    public ResponseEntity<?> withdraw(@CookieValue(value = "access-token", required = false) String token, HttpServletResponse response) {
        try {
            // 토큰에서 유저 뽑아내기
            Member member = memberSerivceImpl.findByAccessToken(token);

            // 로그아웃 처리
            Cookie cookie = new Cookie("access-token", "");
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // HTTPS를 사용하는 경우에만 설정
            cookie.setPath("/");
            cookie.setMaxAge(0); // 쿠키 삭제
            response.addCookie(cookie);

            // 탈퇴
            authService.withdraw(member);

            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.WITHDRAWAL_USER), HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
