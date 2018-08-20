package com.kramphub.datastore.exception;

public class EntityNotFoundException extends RuntimeException {
    private static final ErrorMessages errorMessage = ErrorMessages.ENTITY_NOT_FOUND;

    public EntityNotFoundException(String arg) {
        super(String.format(errorMessage.getMessage(), arg));
    }

    public String getErrorMessage() {
        return errorMessage.getMessage();
    }

    public String getErrorCode() {
        return errorMessage.getCode();
    }
}
