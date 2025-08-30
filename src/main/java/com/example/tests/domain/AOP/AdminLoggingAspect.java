package com.example.tests.domain.AOP;

import com.example.tests.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminLoggingAspect {
    private final ObjectMapper objectMapper; // Jackson 사용

    private static final Logger logger = LoggerFactory.getLogger(AdminApiLoggingAspect.class);

    @Around("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public Object logAdminApi(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime requestTime = LocalDateTime.now();
        HttpServletRequest request = getCurrentHttpRequest();
        String url = request != null ? request.getRequestURI() : "Unknown";

        String requestBody = extractRequestBody(joinPoint);
        User user = getCurrentUser(); // 세션 or SecurityContext 기반

        logger.info("[Admin API Request] time={}, userId={}, url={}, body={}",
                requestTime, user != null ? user.getId() : "Anonymous", url, requestBody);

        Object response = joinPoint.proceed();

        String responseBody = objectMapper.writeValueAsString(response);
        logger.info("[Admin API Response] time={}, url={}, response={}",
                LocalDateTime.now(), url, responseBody);

        return response;
    }

    private String extractRequestBody(ProceedingJoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg != null && !(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)) {
                    return objectMapper.writeValueAsString(arg);
                }
            }
        } catch (Exception e) {
            return "Failed to serialize request body";
        }
        return "No request body";
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    private User getCurrentUser() {
        // 예시: 세션 기반 인증
        HttpServletRequest request = getCurrentHttpRequest();
        if (request != null) {
            return (User) request.getSession().getAttribute("LOGIN_USER");
        }
        return null;
    }

}
