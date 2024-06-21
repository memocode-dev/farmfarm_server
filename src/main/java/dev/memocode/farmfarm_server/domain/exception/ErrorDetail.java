package dev.memocode.farmfarm_server.domain.exception;

public interface ErrorDetail {
    String getErrorCode();
    String getDefaultMessage();
    ErrorCodeLogLevel getLogLevel();
}
