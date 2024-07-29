package org.infinity.sixtalebackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.infinity.sixtalebackend.domain.chat.domain.WaitingChatLog;
import org.infinity.sixtalebackend.domain.chat.dto.ChatMessageRequest;
import org.infinity.sixtalebackend.domain.chat.service.WaitingLogService;
import org.infinity.sixtalebackend.domain.chat.service.WaitingLogServiceImpl;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.repository.MemberRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@Controller
@CrossOrigin(value = "*")
public class ChatController {
    private final WaitingLogService waitingLogService;

//    @MessageMapping("/chat/message")
//    public void message(
//            ChatMessageRequest chatMessageRequest
//    ) {
//        Member member = memberRepository.findById(chatMessageRequest.getMemberID())
//                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + chatMessageRequest.getMemberID()));
//        System.out.println(member.getNickname());
//
//        chatMessageService.sendMessage(chatMessageRequest, member);
//    }

    @MessageMapping("/waiting/chat/message")
    @SendTo("/sub/waiting/chat")
    public void handleWaitingChatMessage(ChatMessageRequest chatMessageRequest) {
        // 대기방 채팅 처리
        waitingLogService.sendWaitingChatMessage(chatMessageRequest);
    }

    @MessageMapping("/waiting/whisper/message")
    @SendTo("/sub/waiting/whisper")
    public void handleWaitingWhisperMessage(ChatMessageRequest chatMessageRequest) {
        // 대기방 귓속말 처리
        waitingLogService.sendWaitingChatMessage(chatMessageRequest);
    }

//    @MessageMapping("/chat/play/message")
//    public void messagePlay(PlayChatMessageRequest chatMessageRequest) {
//        Member member = memberRepository.findById(chatMessageRequest.getMemberID())
//                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + chatMessageRequest.getMemberID()));
//
//        chatMessageService.sendPlayMessage(chatMessageRequest, member);
//    }
}
