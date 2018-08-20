package com.kramphub.datastore.exception;

public class InvalidEntityException extends RuntimeException {
    private static final ErrorMessages errorMessage = ErrorMessages.INVALID_ENTITY;

    public InvalidEntityException(String arg) {
        super(String.format(errorMessage.getMessage(), arg));
    }

    public String getErrorMessage() {
        return errorMessage.getMessage();
    }

    public String getErrorCode() {
        return errorMessage.getCode();
    }
}
