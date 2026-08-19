package com.aiintegration.aiintegrationrecap.controllers;

import com.aiintegration.aiintegrationrecap.dto.LoginDTO;
import com.aiintegration.aiintegrationrecap.dto.SignUpRequestDTO;
import com.aiintegration.aiintegrationrecap.models.User;
import com.aiintegration.aiintegrationrecap.security.JwtUtils;
import com.aiintegration.aiintegrationrecap.security.UserDetailsImp;
import com.aiintegration.aiintegrationrecap.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        User user=new User(null, signUpRequestDTO.getUserName(),signUpRequestDTO.getPassword(),signUpRequestDTO.getEmail());
        return ResponseEntity.ok().body(userService.createUser(user));
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        try {

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword());
            var authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            UserDetailsImp userDetails= (UserDetailsImp) authentication.getPrincipal();
            String jwtToken=jwtUtils.generateTokenFromUserDetails(userDetails);
            ResponseCookie cookie=ResponseCookie.from("jwtCookie",jwtToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).body("Logged In Succesfully!!!");
        }
        catch (AuthenticationException authenticationException){
            return ResponseEntity.badRequest().body("Invalid Credentials!!");
        }
    }
}
