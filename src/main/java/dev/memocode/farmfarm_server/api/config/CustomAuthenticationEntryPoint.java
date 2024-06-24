package dev.memocode.farmfarm_server.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.memocode.farmfarm_server.api.exception.ErrorResponse;
import dev.memocode.farmfarm_server.domain.exception.BaseErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(BaseErrorCode.UNAUTHENTICATED.getErrorCode())
                .message(BaseErrorCode.UNAUTHENTICATED.getDefaultMessage())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
