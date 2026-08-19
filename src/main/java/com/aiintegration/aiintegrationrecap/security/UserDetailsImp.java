package com.aiintegration.aiintegrationrecap.security;

import com.aiintegration.aiintegrationrecap.models.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UserDetailsImp implements org.springframework.security.core.userdetails.UserDetails {
    private String userName;
    private String password;
    private String email;
    public UserDetailsImp(String userName, String password,String email) {
        this.userName = userName;
        this.password = password;
        this.email=email;
    }

    public static UserDetailsImp buildUserDetails(User user){
        return new UserDetailsImp(user.getUserName(),user.getPassword(),user.getEmail());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
    @Override
    public @Nullable String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
