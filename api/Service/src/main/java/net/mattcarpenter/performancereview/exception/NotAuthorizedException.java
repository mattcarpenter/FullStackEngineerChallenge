package net.mattcarpenter.performancereview.exception;

import net.mattcarpenter.performancereview.error.ErrorCode;

public class NotAuthorizedException extends RuntimeException {

    private ErrorCode errorCode;

    public NotAuthorizedException(String message) {
        super(message);
        this.errorCode = ErrorCode.BAD_REQUEST;
    }

    public NotAuthorizedException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public NotAuthorizedException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
