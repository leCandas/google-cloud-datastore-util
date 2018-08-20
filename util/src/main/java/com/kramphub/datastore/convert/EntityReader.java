package com.kramphub.datastore.convert;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import com.kramphub.datastore.exception.EntityNotFoundException;
import com.kramphub.datastore.exception.EntityParseException;
import com.kramphub.datastore.kind.BaseKind;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kramphub.datastore.util.ValidationUtil.getEntityClassName;
import static com.kramphub.datastore.util.ValidationUtil.validateEntityOrThrow;

@SuppressWarnings("unused")
public class EntityReader {

    private final Logger log;

    public EntityReader(Logger log) {
        this.log = log;
    }

    /**
     * This method returns id if exists
     *
     * @param entity
     * @return id if exists
     * @throws EntityNotFoundException
     */
    public Optional<Long> tryGetKey(Entity entity) throws EntityNotFoundException {
        validateEntityOrThrow(entity);
        try {
            return entity.getKey().hasId() ? Optional.of(entity.getKey().getId()) : Optional.empty();
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        }
    }

    /**
     * This method returns name if exists
     *
     * @param entity
     * @return name if exists
     * @throws EntityNotFoundException
     */
    public Optional<String> tryGetName(Entity entity) throws EntityNotFoundException {
        validateEntityOrThrow(entity);
        try {
            return entity.getKey().hasName() ? Optional.of(entity.getKey().getName()) : Optional.empty();
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        }
    }

    /**
     * This method returns entity value of the field
     *
     * @param entity
     * @param field
     * @return field value
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    public String tryGetString(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            return entity.getString(field.value());
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted to %s", field.value(), getEntityClassName(entity), String.class.getSimpleName());
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }

    /**
     * This method returns entity value of the field
     *
     * @param entity
     * @param field
     * @return field value
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    public LatLng tryGetLatLng(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            return entity.getLatLng(field.value());
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted to %s", field.value(), getEntityClassName(entity), LatLng.class.getSimpleName());
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }

    /**
     * This method returns entity value of the field
     *
     * @param entity
     * @param field
     * @return field value
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    public Integer tryGetInteger(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            return Long.valueOf(entity.getLong(field.value())).intValue();
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted to %s", field.value(), getEntityClassName(entity), Integer.class.getSimpleName());
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }

    /**
     * This method returns entity value of the field
     *
     * @param entity
     * @param field
     * @return field value
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    public Long tryGetLong(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            return entity.getLong(field.value());
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted to %s", field.value(), getEntityClassName(entity), Long.class.getSimpleName());
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }

    /**
     * This method returns entity value of the field
     *
     * @param entity
     * @param field
     * @return field value
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    public Timestamp tryGetTimestamp(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            return entity.getTimestamp(field.value());
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted to %s", field.value(), getEntityClassName(entity), Timestamp.class.getSimpleName());
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }

    /**
     * This method returns entity value of the field
     *
     * @param entity
     * @param field
     * @return Instant of field value
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    public Instant tryGetInstant(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            Timestamp timestamp = entity.getTimestamp(field.value());
            return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted to %s", field.value(), getEntityClassName(entity), Timestamp.class.getSimpleName());
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }

    /**
     * This method returns entity value of the field
     *
     * @param entity
     * @param field
     * @return field value
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    public Boolean tryGetBoolean(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            return entity.getBoolean(field.value());
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted to %s", field.value(), getEntityClassName(entity), Boolean.class.getSimpleName());
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }

    /**
     * This method returns entity value of the field
     *
     * @param entity
     * @param field
     * @return field value
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    public Double tryGetDouble(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            return entity.getDouble(field.value());
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted to %s", field.value(), getEntityClassName(entity), Double.class.getSimpleName());
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }

    /**
     * This method returns entity value of the field
     *
     * @param entity
     * @param field
     * @return field value
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    public Blob tryGetBlob(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            return entity.getBlob(field.value());
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted to %s", field.value(), getEntityClassName(entity), Blob.class.getSimpleName());
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }

    /**
     * This generıc method returns entity value of the field
     *
     * @param entity
     * @param field
     * @return field value
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    @SuppressWarnings("unchecked")
    public <T> T tryGetValue(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            return (T) entity.getValue(field.value()).get();
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted", field.value(), getEntityClassName(entity));
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }


    /**
     * This method returns list for given field
     *
     * @param entity
     * @param field
     * @return list
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    public <T> List<T> tryGetList(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            List<Value<T>> list = entity.getList(field.value());
            return list.stream().map(Value::get).collect(Collectors.toList());
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted to %s", field.value(), getEntityClassName(entity), List.class.getSimpleName());
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }

    /**
     * This method returns entity for given field
     *
     * @param entity
     * @param field
     * @return Entity
     * @throws EntityNotFoundException
     * @throws EntityParseException
     */
    public FullEntity<IncompleteKey> tryGetEntity(Entity entity, BaseKind field) throws EntityNotFoundException, EntityParseException {
        validateEntityOrThrow(entity);
        try {
            FullEntity<IncompleteKey> fieldEntity = entity.getEntity(field.value());
            return fieldEntity;
        } catch (DatastoreException e) {
            log.error("Datastore access error for {}", getEntityClassName(entity), e);
            throw e;
        } catch (ClassCastException e) {
            String message = String.format("Field [%s] of %s can not be casted to %s", field.value(), getEntityClassName(entity), List.class.getSimpleName());
            log.error(message, e);
            throw new EntityParseException(message);
        }
    }
}
