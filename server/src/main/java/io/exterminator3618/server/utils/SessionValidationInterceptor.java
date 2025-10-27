package io.exterminator3618.server.utils;

import io.exterminator3618.server.services.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public final class SessionValidationInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionToken = request.getHeader("Authorization");
        if (sessionToken == null || sessionToken.isEmpty()) {
            throw new InvalidRequestException("Missing or empty session token.");
        }
        Long accountId = sessionService.getSessionAccountId(sessionToken);
        if (accountId == null) {
            response.sendError(403, "Invalid or expired session token.");
            return false;
        }
        request.setAttribute("userId", accountId);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
