package com.aiintegration.aiintegrationrecap.security;

import com.aiintegration.aiintegrationrecap.models.User;
import com.aiintegration.aiintegrationrecap.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImp implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailsServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsImp loadUserByUsername(String username) throws UsernameNotFoundException {
         User user=userRepository.findByUserName(username).orElseThrow(()->new RuntimeException("User not Found with userName"+username));
         return UserDetailsImp.buildUserDetails(user);
    }
}
