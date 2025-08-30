package com.example.tests.domain.AOP;

import com.example.tests.domain.auth.service.AuthService;

import com.example.tests.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.apache.tomcat.util.digester.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.management.relation.Role;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AdminApiInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AdminApiInterceptor.class);
    private final AuthService authService; // 세션/토큰 기반 유저 확인 서비스

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = authService.getCurrentUser(); // 현재 로그인 유저 확인

        if (user == null || !user.getRule().equals(Role.ADMIN)) {
            throw new AccessDeniedException("어드민 권한이 필요합니다.");
        }

        logger.info("[Admin API Access] Time: {}, URI: {}, Admin ID: {}",
                LocalDateTime.now(), request.getRequestURI(), user.getId());

        return true;
    }


}
