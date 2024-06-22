package dev.memocode.farmfarm_server.domain.exception;


import lombok.Getter;

import static dev.memocode.farmfarm_server.domain.exception.ErrorCodeLogLevel.INFO;

@Getter
public enum HouseSectionErrorCode implements ErrorDetail {
    NOT_FOUND_HOUSE_SECTION("하우스동을 찾을 수 없습니다.", INFO),
    INVALID_HOUSE_SECTION_RELATION("The house section does not belong to the specified house", INFO),
    NOT_HEALTHY_HOUSE("하우스가 건강하지 않습니다.", INFO),
    ;

    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    HouseSectionErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}