package com.example.shopgiayonepoly.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Ghi nhận thông tin lỗi
        System.out.println("Access denied for user: " + request.getUserPrincipal().getName());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập vào trang này.");
    }
}
