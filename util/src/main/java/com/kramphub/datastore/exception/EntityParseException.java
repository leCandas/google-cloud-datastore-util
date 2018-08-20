package com.kramphub.datastore.exception;

public class EntityParseException extends RuntimeException {
    private static final ErrorMessages errorMessage = ErrorMessages.UNABLE_TO_PARSE_ENTITY_FROM_DATASTORE;

    public EntityParseException(String arg) {
        super(String.format(errorMessage.getMessage(), arg));
    }

    public String getErrorMessage() {
        return errorMessage.getMessage();
    }

    public String getErrorCode() {
        return errorMessage.getCode();
    }
}
