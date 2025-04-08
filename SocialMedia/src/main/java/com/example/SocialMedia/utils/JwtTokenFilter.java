package com.example.SocialMedia.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // If the Authorization header of the request doesn’t contain a Bearer token,
        // It continues the filter chain without updating authentication context.
        if(!hasAuthorizationBearer(request)){
            filterChain.doFilter(request, response);
            return;
        }

        // Else, if the token is not verified, continue the filter chain without updating authentication context.
        String token = JwtTokenUtil.getAccessToken(request);
        if(!jwtTokenUtil.validateAccessToken(token)){
            filterChain.doFilter(request, response);
            return;
        }

        //If the token is verified, update the authentication context with the user details ID and email/phone.
        setAuthenticationContext(token, request);
        //In other words, it tells Spring that the user is authenticated, and continue the downstream filters.
        filterChain.doFilter(request, response);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")){
            return false;
        }

        return true;
    }


    private void setAuthenticationContext(String token, HttpServletRequest request){
        UserDetails userDetails = jwtTokenUtil.getUserDetails(token);
        UsernamePasswordAuthenticationToken authenticationToken =new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
