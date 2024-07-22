package org.infinity.sixtalebackend.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.member.dto.MemberDetailRequestDto;
import org.infinity.sixtalebackend.domain.member.service.MemberDetailService;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/details")
public class MemberDetailController {
    private MemberDetailService memberDetailService;

    /**
     * 회원 상세 정보 생성
     */
    @PostMapping("")
    public ResponseEntity createMemberDetail(@RequestBody MemberDetailRequestDto memberDetailRequestDto, Long memberID){
        try {
            memberDetailService.createMemberDetail(memberDetailRequestDto,memberID);
            return  new ResponseEntity(DefaultResponse.res(StatusCode.CREATED,ResponseMessage.CREATED_MEMBER_DETAIL,null),HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 상세 정보 수정
     */
    @PutMapping("")
    public ResponseEntity updateMemberDetail(@RequestBody MemberDetailRequestDto memberDetailRequestDto, Long memberID){
        try {
            memberDetailService.updateMemberDetail(memberDetailRequestDto,memberID);
            return  new ResponseEntity(DefaultResponse.res(StatusCode.OK,ResponseMessage.UPDATED_MEMBER_DETAIL,null),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
