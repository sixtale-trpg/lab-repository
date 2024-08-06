package org.infinity.sixtalebackend.domain.scenario.controller;

import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.member.dto.MemberResponseDto;
import org.infinity.sixtalebackend.domain.scenario.dto.ScenarioListResponseDto;
import org.infinity.sixtalebackend.domain.scenario.dto.ScenarioResponseDto;
import org.infinity.sixtalebackend.domain.scenario.service.ScenarioService;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scenarios")
public class ScenarioController {

    private final ScenarioService scenarioService;

    /**
     * 시나리오 목록 조회
     * 정렬 기준 : 좋아요수, 등록 일시
     * 필터링 기준 : 장르
     * 검색 : 시나리오 제목
     * 인증 없이도 보이도록함
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getScenarioList(
            @RequestParam(required = false) Long genreID,
            @RequestParam(required = false) String title,
            Pageable scenarioPageable){
        try {
            Long memberID = 1L;
            // 로그인 중인지 아닌지의 로직
            // Long memberId = (userPrincipal != null) ? userPrincipal.getId() : null;
            ScenarioListResponseDto scenarioList = scenarioService.getScenarioList(memberID,genreID,title,scenarioPageable);
            return  new ResponseEntity<>(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_SCENARIO_LIST,scenarioList), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 시나리오 상세 정보 조회
     * @return
     */
    @GetMapping("/{scenarioID}")
    public ResponseEntity<?> getScenarioInfo(@PathVariable Long scenarioID){
        try {
            Long memberID = 1L;
            ScenarioResponseDto scenarioResponseDto = scenarioService.getScenarioInfo(scenarioID,null);
            return  new ResponseEntity<>(DefaultResponse.res(StatusCode.OK, ResponseMessage.CREATED_MEMBER_INFO,scenarioResponseDto), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}