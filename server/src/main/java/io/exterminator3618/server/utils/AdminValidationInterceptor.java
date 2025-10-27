package io.exterminator3618.server.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public final class AdminValidationInterceptor implements HandlerInterceptor {

    private final boolean allowAdminAccess;
    private final String adminAccessToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!allowAdminAccess) {
            throw new InvalidRequestException("Admin access is disabled.");
        }
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.equals(adminAccessToken)) {
            throw new InvalidRequestException("Invalid admin access token.");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
