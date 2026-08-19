package com.aiintegration.aiintegrationrecap.services;

import com.aiintegration.aiintegrationrecap.models.Conversation;
import com.aiintegration.aiintegrationrecap.models.Messages;
import com.aiintegration.aiintegrationrecap.models.User;
import com.aiintegration.aiintegrationrecap.repositories.ConversationRepository;
import com.aiintegration.aiintegrationrecap.repositories.UserRepository;
import com.aiintegration.aiintegrationrecap.security.UserDetailsImp;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.util.Map;
import java.util.Objects;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ChatService chatService;
    private final UserRepository userRepository;

    public ConversationService(ConversationRepository conversationRepository, ChatService chatService, UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.chatService = chatService;
        this.userRepository = userRepository;
    }

    public Map<String, Object> getThroughConversation(String prompt, HttpServletRequest request){
        Cookie cookie= WebUtils.getCookie(request,"conversationId");
        UserDetailsImp userDetailsImp= (UserDetailsImp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assert userDetailsImp != null;
        User user=userRepository.findByUserName(userDetailsImp.getUsername()).orElseThrow(()->new RuntimeException("User doesnt exist!"));
        Conversation conversation;
        if(cookie==null){
            conversation=new Conversation();
            conversation.setUser(user);
            conversation=conversationRepository.save(conversation);
        }
        else{
            conversation=conversationRepository.findByIdAndUser_Id(Long.valueOf(cookie.getValue()),user.getId()).orElseThrow(()->new RuntimeException("ConversationId Not Found!!"));
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
