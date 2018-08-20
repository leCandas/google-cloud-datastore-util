package com.kramphub.datastore.exception;

public enum ErrorMessages {
    ENTITY_NOT_FOUND("Entity not found for given criteria: %s"),
    UNABLE_TO_PARSE_ENTITY_FROM_DATASTORE("Unable to parse entity from Datastore: %s"),
    INVALID_ENTITY("Unable to process entity: %s");

    private String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getCode() {
        return name();
    }

    public String getMessage() {
        return message;
    }
}
