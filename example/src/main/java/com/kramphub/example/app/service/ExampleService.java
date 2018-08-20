package com.kramphub.example.app.service;

import com.google.cloud.datastore.Key;
import com.kramphub.datastore.exception.EntityNotFoundException;
import com.kramphub.datastore.exception.InvalidEntityException;
import com.kramphub.example.datastore.entity.ExampleEntity;
import com.kramphub.example.datastore.repository.ExampleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ExampleService {
    private final ExampleRepository exampleRepository;

    public ExampleService(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void testDatastore() throws InterruptedException {
        ExampleEntity entity = ExampleEntity.builder().id("1").field("field").build();
        Key key = exampleRepository.createKey(entity);
        exampleRepository.upsert(entity.toEntity(key));

        TimeUnit.SECONDS.sleep(10);

        try {
            ExampleEntity entityFromDatestore = exampleRepository.findById(entity.getKey());
            log.info("Saved status -> " + String.valueOf(entityFromDatestore != null));
            log.info("Entity -> " + entityFromDatestore.toString());
        } catch (InvalidEntityException | EntityNotFoundException e) {
            log.error("Saved status -> false", e);
        }

        log.info("All entities of kind -> {}", exampleRepository.findAll().toString());
        log.info("Removing all entities of kind -> {}", exampleRepository.deleteAll());
        log.info("All entities left of kind -> {}", exampleRepository.findAll().toString());
    }
}
