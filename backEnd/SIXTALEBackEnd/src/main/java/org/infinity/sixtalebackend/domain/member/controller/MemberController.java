package org.infinity.sixtalebackend.domain.member.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.member.dto.MemberResponseDto;
import org.infinity.sixtalebackend.domain.member.service.MemberSerivceImpl;
import org.infinity.sixtalebackend.domain.member.dto.MemberNicknameCheckResponse;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.infinity.sixtalebackend.infra.s3.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberSerivceImpl memberSerivce;

    /**
     * 회원 정보 생성(닉네임, 프로필 이미지)
     */
    @PostMapping(value = "",consumes = "multipart/*")
    public ResponseEntity<?> createMemberInfo(String nickName, @RequestPart("files")MultipartFile[] files){
        try {
            Long memberId = 1L;
            MemberResponseDto memberResponseDto = memberSerivce.createMemberInfo(memberId,nickName,files);
            return  new ResponseEntity<>(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPDATED_MEMBER_DETAIL,memberResponseDto),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 정보 조회(닉네임, 프로필 이미지)
     */
    @GetMapping("")
    public ResponseEntity<?> getMemberInfo(){
        try {
            Long memberId = 1L;
            MemberResponseDto memberResponseDto = memberSerivce.getMemberInfo(memberId);
            return  new ResponseEntity<>(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPDATED_MEMBER_DETAIL,memberResponseDto),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 정보 수정(닉네임, 프로필 이미지)
     */
    @PutMapping(value = "",consumes = "multipart/*")
    public ResponseEntity<?> updateMemberInfo(String nickName, @RequestPart("files")MultipartFile[] files){
        try {
            Long memberId = 1L;
            MemberResponseDto memberResponseDto = memberSerivce.updateMemberInfo(memberId,nickName,files);
            return  new ResponseEntity<>(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPDATED_MEMBER_DETAIL,memberResponseDto),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/check-nickname")
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
