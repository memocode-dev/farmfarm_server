package dev.memocode.farmfarm_server.domain.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException implements ErrorCodedException {

    private final ErrorDetail errorDetail;

    public NotFoundException(ErrorDetail errorDetail) {
        super(errorDetail.getDefaultMessage());
        this.errorDetail = errorDetail;
    }
}
