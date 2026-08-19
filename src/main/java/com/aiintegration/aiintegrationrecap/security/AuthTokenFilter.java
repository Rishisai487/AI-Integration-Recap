package com.aiintegration.aiintegrationrecap.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImp userDetailsServiceImp;

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImp userDetailsServiceImp) {
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie= WebUtils.getCookie(request,"jwtCookie");
        if(cookie==null){
            filterChain.doFilter(request,response);
            return;
        }
        String jwtToken=cookie.getValue();
        if(jwtUtils.verifyJwtToken(jwtToken)){
            String userName= jwtUtils.getUserNameFromToken(jwtToken);
            UserDetails userDetails=userDetailsServiceImp.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken
                    (userDetails,null,userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);
    }
}
