package org.infinity.sixtalebackend.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.dto.AuthResponse;
import org.infinity.sixtalebackend.domain.member.service.OAuth2UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class AuthController {

    OAuth2UserServiceImpl oAuth2UserService;

    public AuthController(OAuth2UserServiceImpl oAuth2UserService, HttpSession httpSession) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @GetMapping("/auth/login/{registrationID}")
    public ResponseEntity<?> login(@RequestParam String code, @PathVariable String registrationID, HttpServletRequest request) {
        Member member = oAuth2UserService.socialLogin(code, registrationID);
        HttpSession session = request.getSession();
        session.setAttribute("user", member);
        return ResponseEntity.ok(code + " " + registrationID + " " + member.getId() + " " + member.getEmail() + " " + member.getNickname() + " " + member.getIsWithdrawn());
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("LOGOUT OK");
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<?> withdraw(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute("user");
        if (member != null) {
            oAuth2UserService.withdraw(member);
            session.invalidate();
        }
        return ResponseEntity.ok("WITHDRAW OK");
    }

//    @GetMapping("/success")
//    public ResponseEntity<String> success(@AuthenticationPrincipal OAuth2User oAuth2User, HttpSession httpSession) {
//
//        return ResponseEntity.ok("success");
//    }
//
//    @GetMapping("/fail")
//    public ResponseEntity<?> fail() {
//        return new ResponseEntity<String>("FAIL", HttpStatus.OK);
//    }
}
