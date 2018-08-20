package com.kramphub.datastore.kind;

public interface BaseKind {
    /**
     * This method returns the filed value.
     *
     * @return String filed name
     */
    String value();

    /**
     * Kind identifier for the entities
     *
     * @return String kind
     */
    String getKindIdentifier();
}
