package com.example.currencyconverter.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${app.security.api-key}")
    private String expectedApiKey;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String key = request.getHeader("X-API-KEY");

        if (expectedApiKey.equals(key)) {
            return true;
        }

        response.setStatus(401);
        response.setContentType("application/json");

        response.getWriter().write(
            "{\"error\":\"Invalid API Key\"}"
        );

        return false;
    }
}