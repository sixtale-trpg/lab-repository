package org.infinity.sixtalebackend.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.member.service.MemberSerivceImpl;
import org.infinity.sixtalebackend.domain.member.dto.MemberNicknameCheckResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberSerivceImpl memberSerivce;

    @GetMapping("/members/check-nickname")
    public ResponseEntity checkNickname(@RequestParam String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            return new ResponseEntity<>(new MemberNicknameCheckResponse(400, "잘못된 요청", null),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            boolean isDuplicated = memberSerivce.isNicknameDuplicated(nickname);
            if (isDuplicated) {
                return new ResponseEntity<>(new MemberNicknameCheckResponse(200, "닉네임 중복 검사 성공 (중복O)", new MemberNicknameCheckResponse.Data(false)),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new MemberNicknameCheckResponse(200, "닉네임 중복 검사 성공 (중복X)", new MemberNicknameCheckResponse.Data(true)),
                        HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new MemberNicknameCheckResponse(500, "서버 에러", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
