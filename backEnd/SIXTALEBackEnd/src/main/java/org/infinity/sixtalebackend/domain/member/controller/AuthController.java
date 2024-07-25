package org.infinity.sixtalebackend.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.service.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class AuthController {

    AuthServiceImpl authService;

    public AuthController(AuthServiceImpl oAuth2UserService, HttpSession httpSession) {
        this.authService = oAuth2UserService;
    }

    @GetMapping("/auth/login/{registrationID}")
    public ResponseEntity<?> login(@RequestParam String code, @PathVariable String registrationID, HttpServletRequest request) {
        Member member = authService.socialLogin(code, registrationID);
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
            authService.withdraw(member);
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
