package org.infinity.sixtalebackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageListResponse;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.infinity.sixtalebackend.domain.chat.dto.GameMessageDto;
import org.infinity.sixtalebackend.domain.chat.service.PlayGameLogService;
import org.infinity.sixtalebackend.domain.chat.service.PlayLogService;
import org.infinity.sixtalebackend.global.common.response.DefaultResponse;
import org.infinity.sixtalebackend.global.common.response.ResponseMessage;
import org.infinity.sixtalebackend.global.common.response.StatusCode;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RequiredArgsConstructor
@Controller
@CrossOrigin(value = "*")
public class PlayChatController {
    private final PlayLogService playLogService;
    private final PlayGameLogService playGameLogService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/play/chat/message")
    public void handlePlayChatMessage(ChatMessageRequest chatMessageRequest) {
        // 플레이방 채팅 처리
        playLogService.sendPlayChatMessage(chatMessageRequest);
    }

    /**
     * 방별로 플레이방 일반 채팅 로그 리스트 조회(페이지네이션)
     *
     * @param roomID
     * @param pageable
     * @return
     */
    @GetMapping("/play/chat/log/{roomID}")
    public ResponseEntity<?> getPlayChatLogList(@PathVariable Long roomID, Pageable pageable) {
        try {
            ChatMessageListResponse chatMessageListResponse = playLogService.getPlayChatLogList(roomID,pageable);
            return  new ResponseEntity<>(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_PLAY_CHAT_LOG_LIST,chatMessageListResponse), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 방별로 플레이방 귓속말 채팅 로그 리스트 조회(페이지네이션)
     *
     * @param roomID
     * @param pageable
     * @return
     */
    @GetMapping("/play/chat/whisper/log/{roomID}")
    public ResponseEntity<?> getPlayWhisperChatLogList(@PathVariable Long roomID,Pageable pageable) {
        try {
            Long memberID = 2L;
            ChatMessageListResponse chatMessageListResponse = playLogService.getPlayChatWisperLogList(roomID,memberID,pageable);
            return  new ResponseEntity<>(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_PLAY_WHISPER_CHAT_LOG_LIST,chatMessageListResponse), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전체 플레이방 채팅 로그 리스트 조회(페이지네이션)
     * @param roomID
     * @param pageable
     * @return
     */
    @GetMapping("/play/chat/all/log/{roomID}")
    public ResponseEntity<?> getPlayChatAllLogList(@PathVariable Long roomID,Pageable pageable) {
        try {
            Long memberID = 2L;
            ChatMessageListResponse chatMessageListResponse = playLogService.getPlayChatAllLogList(roomID,memberID,pageable);
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_PLAY_ALL_CHAT_LOG_LIST,chatMessageListResponse), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(DefaultResponse.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 플레이 게임 로그
     * @return
     */
    @MessageMapping("/game/message")
    // @SendTo("/sub/game/messages/{roomID}")
    public void handleGameMessage(GameMessageDto gameMessageDto){
        // 로그 저장 및 처리
        log.info("FFFFFFFFFFFFFFFFF", gameMessageDto.getRoomID());

        // 처리된 메시지를 클라이언트에게 전송x
        playGameLogService.sendPlayGameLogMessage(gameMessageDto);

        // roomID를 포함한 구독 주소로 메시지 전송
        messagingTemplate.convertAndSend("/sub/game/messages/" + gameMessageDto.getRoomID(), gameMessageDto);
    }
}
