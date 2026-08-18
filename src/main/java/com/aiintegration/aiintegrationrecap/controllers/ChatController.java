package com.aiintegration.aiintegrationrecap.controllers;

import com.aiintegration.aiintegrationrecap.services.ChatService;
import com.aiintegration.aiintegrationrecap.services.ConversationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ConversationService conversationService;
    public ChatController( ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ResponseEntity<Object> chat(@RequestBody String prompt, HttpServletRequest request){
        Map<String,Object> response=conversationService.getThroughConversation(prompt,request);
        ResponseCookie responseCookie= ResponseCookie.from("conversationId",response.get("conversationId").toString())
                .secure(true)
                .path("/")
                .httpOnly(true)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString()).body(response.get("responseMessage"));
    }
}
