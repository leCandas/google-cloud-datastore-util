package com.kramphub.example.datastore.repository;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.kramphub.datastore.exception.EntityNotFoundException;
import com.kramphub.datastore.exception.EntityParseException;
import com.kramphub.datastore.exception.InvalidEntityException;
import com.kramphub.datastore.kind.BaseKind;
import com.kramphub.datastore.repository.DatastoreRepository;
import com.kramphub.example.datastore.entity.ExampleEntity;
import com.kramphub.example.datastore.kind.ExampleKind;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExampleRepository extends DatastoreRepository<ExampleEntity> {

    public ExampleRepository(Datastore datastore) {
        super(datastore, log, 100);
    }

    @Override
    public BaseKind getKind() {
        return ExampleKind.ID;
    }

    @Override
    protected ExampleEntity map(Entity entity) throws InvalidEntityException {
        try {
            return ExampleEntity.builder()
                    .id(entityReader.tryGetString(entity, ExampleKind.ID))
                    .field(entityReader.tryGetString(entity, ExampleKind.FIELD))
                    .build();
        } catch (EntityNotFoundException | EntityParseException e) {
            throw new InvalidEntityException(e.getMessage());
        }
    }


}
