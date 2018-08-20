package com.kramphub.datastore.util;

import com.google.cloud.datastore.Entity;
import com.kramphub.datastore.exception.EntityNotFoundException;

import java.util.Collection;

@SuppressWarnings("unused")
public class ValidationUtil {

    /**
     * This method checks if the entity is not null and has a key, returns boolean as a result
     *
     * @param entity
     * @return boolean
     */
    public static boolean validateEntity(Entity entity) {
        return (entity != null && entity.getKey() != null);
    }

    /**
     * This method checks if the entity is not null and has a key, throws exception if not
     *
     * @param entity
     * @throws EntityNotFoundException
     */
    public static void validateEntityOrThrow(Entity entity) throws EntityNotFoundException {
        if (entity == null || entity.getKey() == null) {
            throw new EntityNotFoundException(getEntityClassName(entity));
        }
    }

    /**
     * This method checks if the entities are not null and has a key, throws exception if not
     *
     * @param entities
     * @throws EntityNotFoundException
     */
    public static void validateEntitiesOrThrow(Collection<Entity> entities) throws EntityNotFoundException {
        for (Entity entity : entities) {
            validateEntityOrThrow(entity);
        }
    }

    public static String getEntityClassName(Entity entity) {
        return (entity == null ? "" : entity.getClass().getSimpleName());
    }
}
