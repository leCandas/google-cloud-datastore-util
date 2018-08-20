package com.kramphub.example.datastore.kind;

import com.kramphub.datastore.kind.BaseKind;

public enum ExampleKind implements BaseKind {
    ID("id"),
    FIELD("field");

    private final String value;

    ExampleKind(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String getKindIdentifier() {
        return "example";
    }
}
