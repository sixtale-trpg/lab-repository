package org.infinity.sixtalebackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageListResponse;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.infinity.sixtalebackend.domain.chat.service.WaitingLogService;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class WaitingChatController {
    private final WaitingLogService waitingLogService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/waiting/chat/message")
    public void handleWaitingChatMessage(ChatMessageRequest chatMessageRequest) {
        // 대기방 채팅 처리
        waitingLogService.sendWaitingChatMessage(chatMessageRequest);

        messagingTemplate.convertAndSend("/sub/waiting/chat/room/" + chatMessageRequest.getRoomID(), chatMessageRequest);
    }

    /**
     * 방별로 대기방 일반 채팅 로그 리스트 조회(페이지네이션)
     *
     * @param roomID
     * @param pageable
     * @return
     */
    @GetMapping("/waiting/chat/log/{roomID}")
    public ResponseEntity<?> getWaitingChatLogList(@PathVariable Long roomID,Pageable pageable) {
        try {
            ChatMessageListResponse chatMessageListResponse = waitingLogService.getWaitingChatLogList(roomID,pageable);
            return  new ResponseEntity<>(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_WAITING_CHAT_LOG_LIST,chatMessageListResponse), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 방별로 대기방 귓속말 채팅 로그 리스트 조회(페이지네이션)
     *
     * @param roomID
     * @param pageable
     * @return
     */
    @GetMapping("/waiting/chat/whisper/log/{roomID}")
    public ResponseEntity<?> getWaitingWhisperChatLogList(@PathVariable Long roomID,Pageable pageable) {
        try {
            Long memberID = 2L;
            ChatMessageListResponse chatMessageListResponse = waitingLogService.getWaitingChatWisperLogList(roomID,memberID,pageable);
            return  new ResponseEntity<>(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_WAITING_WHISPER_CHAT_LOG_LIST,chatMessageListResponse), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전체 대기방 채팅 로그 리스트 조회(페이지네이션)
     * @param roomID
     * @param pageable
     * @return
     */
    @GetMapping("/waiting/chat/all/log/{roomID}")
    public ResponseEntity<?> getWaitingChatAllLogList(@PathVariable Long roomID,Pageable pageable) {
        try {
            Long memberID = 2L;
            ChatMessageListResponse chatMessageListResponse = waitingLogService.getWaitingChatAllLogList(roomID,memberID,pageable);
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_WAITING_ALL_CHAT_LOG_LIST,chatMessageListResponse), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
