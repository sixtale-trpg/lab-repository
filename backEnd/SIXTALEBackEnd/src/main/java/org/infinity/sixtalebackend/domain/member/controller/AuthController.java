package org.infinity.sixtalebackend.domain.member.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.service.AuthServiceImpl;
import org.infinity.sixtalebackend.domain.member.util.JWTUtil;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.http.HttpStatus;
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
            //
            //            // JWT가 유효한 경우, 사용자 정보 추출
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
    public ResponseEntity<?> login(@RequestParam String code, @PathVariable String registrationID, HttpServletRequest request) {
        try {
            Member member = authService.socialLogin(code, registrationID);
            String accessToken = jwtUtil.generateToken(member.getEmail());
            HttpSession session = request.getSession();
            session.setAttribute("accessToken", accessToken);

            log.info("accessToken" + String.valueOf(session.getAttribute("accessToken")));
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS), HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            if (session != null) {
                session.invalidate();
            }
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGOUT_SUCCESS), HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원탈퇴
     */
    @PatchMapping("/withdraw")
    public ResponseEntity<?> withdraw(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            Member accessToken = (Member) session.getAttribute("accessToken");
            if (accessToken != null) {
                authService.withdraw(accessToken);
                session.invalidate();
            }
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.WITHDRAWAL_USER), HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
