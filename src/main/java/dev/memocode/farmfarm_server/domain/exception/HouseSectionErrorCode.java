package dev.memocode.farmfarm_server.domain.exception;


import lombok.Getter;

import static dev.memocode.farmfarm_server.domain.exception.ErrorCodeLogLevel.INFO;

@Getter
public enum HouseSectionErrorCode implements ErrorDetail {
    NOT_FOUND_HOUSE_SECTION("하우스동을 찾을 수 없습니다.", INFO),
    INVALID_HOUSE_SECTION_RELATION("The house section does not belong to the specified house", INFO),
    NOT_HEALTHY_HOUSE_SECTION("하우스동이 건강하지 않습니다.", INFO),
    HOUSE_SECTION_REFERENCED("해당 하우스 섹션을 참조한 객체가 존재하여 삭제할 수 없습니다.", INFO),
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
