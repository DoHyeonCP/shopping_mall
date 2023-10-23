package com.example.mall.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
    AuthenticationException authException) throws IOException, ServletException{

        String requestURI = request.getRequestURI();
        if("XMLHttpRequest".equals(request.getHeader("x-request-with"))){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
        } else if (!"/members/new".equals(requestURI)){
            // 요청 URL이 회원가입 페이지가 아닌 경우에만 로그인 페이지로 리다이렉트
            response.sendRedirect("/members/login");
        }
    }
}
