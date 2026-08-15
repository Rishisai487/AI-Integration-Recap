package com.aiintegration.aiintegrationrecap.controllers;

import com.aiintegration.aiintegrationrecap.services.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public String chat(@RequestBody String prompt){
        return chatService.callGemini(prompt);
    }
}
