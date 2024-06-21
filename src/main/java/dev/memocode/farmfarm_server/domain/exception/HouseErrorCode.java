package dev.memocode.farmfarm_server.domain.exception;


import lombok.Getter;

import static dev.memocode.farmfarm_server.domain.exception.ErrorCodeLogLevel.INFO;

@Getter
public enum HouseErrorCode implements ErrorDetail {
    ALREADY_EXISTS_HOUSE_NAME("이미 존재하는 하우스 이름입니다.", INFO),
    NOT_FOUND_HOUSE("하우스를 찾을 수 없습니다.", INFO),
    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    HouseErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
