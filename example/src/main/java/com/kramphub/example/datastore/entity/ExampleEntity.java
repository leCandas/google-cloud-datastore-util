package com.kramphub.example.datastore.entity;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.kramphub.datastore.entity.BaseEntity;
import com.kramphub.example.datastore.kind.ExampleKind;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExampleEntity extends BaseEntity {
    String id;
    String field;

    @Override
    public Entity toEntity(Key key) {
        return Entity.newBuilder(key)
                .set(ExampleKind.ID.value(), getId())
                .set(ExampleKind.FIELD.value(), getField())
                .build();
    }

    @Override
    public String getKey() {
        return getId();
    }
}
