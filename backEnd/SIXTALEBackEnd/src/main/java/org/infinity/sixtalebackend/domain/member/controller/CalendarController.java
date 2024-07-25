package org.infinity.sixtalebackend.domain.member.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.infinity.sixtalebackend.domain.member.service.CalendarServiceImpl;
import org.infinity.sixtalebackend.domain.member.dto.CalendarListResponse;
import org.infinity.sixtalebackend.domain.member.dto.CalendarRequest;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members/calendars")
@AllArgsConstructor
public class CalendarController {
    private final CalendarServiceImpl calendarService;

    /**
     *  회원 일정 조회(개인)
     */
    @GetMapping
    public ResponseEntity getAllCalendars() {
        try {
            long id = 1L;
            CalendarListResponse response = calendarService.getAllCalendars(id);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, "회원 일정 조회 성공"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, "서버 에러"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 일정 생성
     * @param calendarRequest
     */
    @PostMapping
    public ResponseEntity createCalendar(@RequestBody @Valid CalendarRequest calendarRequest) {
        try {
            // bearer 토큰 추출 대신 id = 1인 유저 가정
            Long id = 1L;
            calendarService.createCalendar(id, calendarRequest);
            return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, "회원 일정 생성 성공"), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, "서버 에러"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 일정 삭제
     * @param calendarId
     */
    @DeleteMapping("/{calendarId}")
    public ResponseEntity deleteCalendar(@PathVariable Long calendarId) {
        try {
            // bearer 토큰 추출 대신 id = 1인 유저 가정
            Long id = 1L;
            calendarService.deleteCalendar(calendarId, id);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, "회원 일정 삭제 성공"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, "서버 에러"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    토큰 있을 때 가정
    @PostMapping
    public ResponseEntity<DefaultResponse<Void>> createCalendar(HttpServletRequest request,
                                                                @RequestBody CalendarRequest calendarRequest) {
        try {
            String token = getTokenFromRequest(request);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(DefaultResponse.res(401, "인증 권한 에러"));
            }
            String email = jwtTokenProvider.getUsername(token);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }*/

}
