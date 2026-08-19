package com.aiintegration.aiintegrationrecap.services;

import com.aiintegration.aiintegrationrecap.models.User;
import com.aiintegration.aiintegrationrecap.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public String createUser(User user){
        Boolean existsOrNot=userRepository.existsByUserName(user.getUserName());
        if(existsOrNot==true){
            return "UserName or Email might already exists! Please Sign in!!";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "SignUp Successful!!";
    }
}
