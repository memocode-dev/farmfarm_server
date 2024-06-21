package dev.memocode.farmfarm_server.api.exception;

import dev.memocode.farmfarm_server.domain.exception.ErrorCodeLogLevel;
import dev.memocode.farmfarm_server.domain.exception.ErrorDetail;
import lombok.Getter;

import static dev.memocode.farmfarm_server.domain.exception.ErrorCodeLogLevel.*;

@Getter
public enum ApiErrorCode implements ErrorDetail {

    PERMISSION_DENIED("권한이 부족합니다.", WARNING),
    UNAUTHENTICATED("인증이 필요합니다.", WARNING),
    INTERNAL_SERVER_ERROR("서버 에러", CRITICAL),
    VALIDATION_ERROR("서버 에러", INFO),
    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    ApiErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
