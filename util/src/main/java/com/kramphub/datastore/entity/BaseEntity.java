package com.kramphub.datastore.entity;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

import java.io.Serializable;

@SuppressWarnings("unused")
public abstract class BaseEntity implements Serializable {

    /**
     * Implement this method so that Data Object can be mapped to Datastore entities
     *
     * @param key
     * @return Datastore entity
     */
    public abstract Entity toEntity(Key key);

    /**
     * Implement the key generation strategy for your entity Long or String types are accepted.
     * Ex: You can use an unique filed as id or
     * combination of fields that makes it to be retrieved without query later on or just UUID
     * Keep in mind that keys must be unique for the kind
     *
     * @return String as name or Long as id
     */
    public abstract Object getKey();
}
