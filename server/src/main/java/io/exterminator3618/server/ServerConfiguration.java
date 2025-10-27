package io.exterminator3618.server;

import io.exterminator3618.server.utils.AdminValidationInterceptor;
import io.exterminator3618.server.utils.SessionValidationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class ServerConfiguration implements WebMvcConfigurer {

    @Value("${exterminator3618.admin.allow-access}")
    public boolean allowAdminAccess;

    @Value("${exterminator3618.admin.access-token}")
    public String adminAccessToken;

    private final SessionValidationInterceptor sessionValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionValidationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/account/**",
                        "/api/system/**"
                );
        registry.addInterceptor(new AdminValidationInterceptor(allowAdminAccess, adminAccessToken))
                .addPathPatterns("/api/admin/**");
    }

}
