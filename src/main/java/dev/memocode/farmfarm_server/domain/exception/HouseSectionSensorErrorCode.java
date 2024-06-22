package dev.memocode.farmfarm_server.domain.exception;


import lombok.Getter;

import static dev.memocode.farmfarm_server.domain.exception.ErrorCodeLogLevel.INFO;

@Getter
public enum HouseSectionSensorErrorCode implements ErrorDetail {
    NOT_FOUND_HOUSE_SECTION_SENSOR("하우스동 센서를 찾을 수 없습니다.", INFO),
    INVALID_HOUSE_SECTION_SENSOR_RELATION(
            "The house section sensor does not belong to the specified house section", INFO),
    ;

    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    HouseSectionSensorErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
