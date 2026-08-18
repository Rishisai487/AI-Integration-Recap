package com.aiintegration.aiintegrationrecap.services;

import com.aiintegration.aiintegrationrecap.models.Conversation;
import com.aiintegration.aiintegrationrecap.models.Messages;
import com.aiintegration.aiintegrationrecap.repositories.ConversationRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.util.Map;
import java.util.Objects;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ChatService chatService;

    public ConversationService(ConversationRepository conversationRepository, ChatService chatService) {
        this.conversationRepository = conversationRepository;
        this.chatService = chatService;
    }

    public Map<String, Object> getThroughConversation(String prompt, HttpServletRequest request){
        Cookie cookie= WebUtils.getCookie(request,"conversationId");
        Conversation conversation;
        if(cookie==null){
            conversation=new Conversation();
            conversation=conversationRepository.save(conversation);
        }
        else{
            conversation=conversationRepository.findById(Long.valueOf(cookie.getValue())).orElseThrow(()->new RuntimeException("ConversationId Not Found!!"));
        }
        Messages messages=new Messages();
        messages.setConversation(conversation);
        messages.setMessage(prompt);
        messages.setRole("user");
        conversation.getMessages().add(messages);
        conversationRepository.save(conversation);
        String responseMessage=chatService.callGemini(conversation.getMessages());
        Messages messages1=new Messages();
        messages1.setRole("model");
        messages1.setMessage(responseMessage);
        messages1.setConversation(conversation);
        conversation.getMessages().add(messages1);
        conversationRepository.save(conversation);
        return Map.of("conversationId",conversation.getId(),"responseMessage",responseMessage);
    }
}
