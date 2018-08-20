package com.kramphub.datastore.repository;

import com.google.cloud.datastore.*;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.kramphub.datastore.convert.EntityReader;
import com.kramphub.datastore.entity.BaseEntity;
import com.kramphub.datastore.exception.EntityNotFoundException;
import com.kramphub.datastore.exception.InvalidEntityException;
import com.kramphub.datastore.kind.BaseKind;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static com.kramphub.datastore.util.ValidationUtil.validateEntitiesOrThrow;
import static com.kramphub.datastore.util.ValidationUtil.validateEntityOrThrow;

@SuppressWarnings("unused")
public abstract class DatastoreRepository<T extends BaseEntity> {
    public final EntityReader entityReader;
    final Logger log;
    final Datastore datastore;
    final KeyFactory keyFactory;
    final Integer defaultQueryLimit;
    private final int MAX_QUERY_LIMIT = 5000;
    private final int DEFAULT_QUERY_LIMIT = 500;

    /**
     * Init with custom query limit max 5000
     *
     * @param datastore
     * @param log
     * @param defaultQueryLimit max 5000
     */
    public DatastoreRepository(Datastore datastore, Logger log, int defaultQueryLimit) {
        this.datastore = datastore;
        this.log = log;
        this.keyFactory = datastore.newKeyFactory().setKind(getKind().getKindIdentifier());
        this.defaultQueryLimit = defaultQueryLimit > MAX_QUERY_LIMIT ? MAX_QUERY_LIMIT : defaultQueryLimit < 1 ? DEFAULT_QUERY_LIMIT : defaultQueryLimit;
        this.entityReader = new EntityReader(log);
    }

    /**
     * Init with default query limit (500)
     *
     * @param datastore
     * @param log
     */
    public DatastoreRepository(Datastore datastore, Logger log) {
        this.datastore = datastore;
        this.log = log;
        this.keyFactory = datastore.newKeyFactory().setKind(getKind().getKindIdentifier());
        this.defaultQueryLimit = DEFAULT_QUERY_LIMIT;
        this.entityReader = new EntityReader(log);
    }

    /**
     * This method saves the given entity to datastore if entity id does not already exist in datastore
     *
     * @param entity
     * @return Entity id, if not exists entity name
     * @throws EntityNotFoundException if entity is null or has no key
     */
    public String add(Entity entity) throws EntityNotFoundException {
        validateEntityOrThrow(entity);
        return add(Collections.singletonList(entity)).get(0);
    }

    /**
     * This method saves the given entities to datastore if entity ids does not already exists in datastore
     *
     * @param entities
     * @return Entity id, if not exists entity name
     * @throws EntityNotFoundException if entity is null or has no key
     */
    public List<String> add(Collection<Entity> entities) throws EntityNotFoundException {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        validateEntitiesOrThrow(entities);

        List<Entity> add = datastore.add(entities.toArray(new Entity[0]));

        return add.stream()
                .map(Entity::getKey)
                .map(Key::getNameOrId)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    /**
     * This method saves or updates the given entity to datastore
     *
     * @param entity
     * @return Entity id, if not exists entity name
     * @throws EntityNotFoundException if entity is null or has no key
     */
    public String upsert(Entity entity) throws EntityNotFoundException {
        validateEntityOrThrow(entity);
        return upsert(Collections.singletonList(entity)).get(0);
    }

    /**
     * This method saves or updates the given entities to datastore
     *
     * @param entities
     * @return Entity id, if not exists entity name
     * @throws EntityNotFoundException if entity is null or has no key
     */
    public List<String> upsert(Collection<Entity> entities) throws EntityNotFoundException {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        validateEntitiesOrThrow(entities);

        List<Entity> put = datastore.put(entities.toArray(new Entity[0]));

        return put.stream()
                .map(Entity::getKey)
                .map(Key::getNameOrId)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    /**
     * This method updates the given entity to datastore
     *
     * @param entity
     * @throws EntityNotFoundException if entity is null or has no key
     */
    public void update(Entity entity) throws EntityNotFoundException {
        validateEntityOrThrow(entity);
        update(Collections.singletonList(entity));
    }

    /**
     * This method updates the given entities in datastore
     *
     * @param entities
     * @throws EntityNotFoundException if entity is null or has no key
     */
    public void update(Collection<Entity> entities) throws EntityNotFoundException {
        if (entities == null || entities.isEmpty()) {
            return;
        }

        validateEntitiesOrThrow(entities);

        datastore.update(entities.toArray(new Entity[0]));
    }

    /**
     * This method deletes entity by the given key from datastore
     *
     * @param key
     * @throws EntityNotFoundException if key is null or has no key
     */
    public void delete(Key key) throws EntityNotFoundException {
        delete(Collections.singletonList(key));
    }

    /**
     * This method deletes entities by their given keys from datastore
     *
     * @param keys
     * @throws EntityNotFoundException if key is null or has no key
     */
    public void delete(Collection<Key> keys) throws EntityNotFoundException {
        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (Key key : keys) {
            if (key == null || key.getNameOrId() == null || ("" + key.getNameOrId()).isEmpty())
                throw new EntityNotFoundException("key");
        }

        datastore.delete(keys.toArray(new Key[0]));
    }

    /**
     * This method finds entity by their given keyId from datastore and maps it
     *
     * @param id
     * @return Mapped entity if found
     * @throws EntityNotFoundException if not found
     */
    public T findById(Long id) throws InvalidEntityException {
        return findByKey(keyFactory.newKey(id));
    }

    /**
     * This method finds entity by their given keyName from datastore and maps it
     *
     * @param name
     * @return Mapped entity if found
     * @throws InvalidEntityException mapping failed
     * @throws EntityNotFoundException if not found
     */
    public T findById(String name) throws InvalidEntityException, EntityNotFoundException {
        return findByKey(keyFactory.newKey(name));

    }

    /**
     * This method checks if the entity exists in data store by its name.
     *
     * @param name
     * @throws EntityNotFoundException if entity does not exist
     */
    public void checkById(String name) throws EntityNotFoundException {
        checkByKey(keyFactory.newKey(name));
    }

    /**
     * This method checks if the entity exists in data store by its id.
     *
     * @param id
     * @throws EntityNotFoundException if entity does not exist
     */
    public void checkById(Long id) throws EntityNotFoundException {
        checkByKey(keyFactory.newKey(id));
    }

    /**
     * This method returns all mapped entities for the kind
     * The query is limited to max 5000 entities which is the actual limit of Datastore
     *
     * @return List of mapped entities
     */
    public List<T> findAll() {
        StructuredQuery<Entity> query = Query.newEntityQueryBuilder()
                .setKind(getKind().getKindIdentifier())
                .setLimit(MAX_QUERY_LIMIT)
                .build();

        return executeQuery(query);
    }

    /**
     * This method returns count of entities
     *
     * @return count
     */
    public long count() {
        return Iterators.size(datastore.run(Query.newKeyQueryBuilder()
                .setKind(getKind().getKindIdentifier()).build()));
    }

    /**
     * This method deletes all entities for the kind
     *
     */
    public boolean deleteAll() {
        try {
            StructuredQuery<Key> query = getKeyQueryBuilder().build();
            List<Key> keys = parseKeyQueryResult(datastore.run(query));
            if (!keys.isEmpty()) {
                delete(keys);
                return deleteAll();
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error("Exception on deleteAll operation!", e);
            return false;
        }
    }


    /**
     * This method return a query builder with given limit
     *
     * @param limit for query return size
     * @return StructuredQuery.Builder
     */
    public StructuredQuery.Builder<Entity> getQueryBuilder(Integer limit) {
        return Query.newEntityQueryBuilder()
                .setKind(getKind().getKindIdentifier())
                .setLimit(limit);
    }

    /**
     * This method return a query builder with default limit
     *
     * @return StructuredQuery.Builder
     */
    public StructuredQuery.Builder<Entity> getQueryBuilder() {
        return Query.newEntityQueryBuilder()
                .setKind(getKind().getKindIdentifier())
                .setLimit(defaultQueryLimit);
    }

    /**
     * This method return a key query builder with max limit
     *
     * @return StructuredQuery.Builder
     */
    public StructuredQuery.Builder<Key> getKeyQueryBuilder() {
        return Query.newKeyQueryBuilder()
                .setKind(getKind().getKindIdentifier())
                .setLimit(MAX_QUERY_LIMIT);
    }

    /**
     * This method needs to be implemented to determine the kind for repository.
     *
     * @return kind of repository
     */
    public abstract BaseKind getKind();

    /**
     * This method needs to be implemented since it will be used on parseQueryResult method.
     *
     * @param entity
     * @return mapped Entity
     * @throws InvalidEntityException
     */
    protected abstract <T> T map(Entity entity) throws InvalidEntityException;

    public Key createKey(BaseEntity baseEntity) {
        Object key = baseEntity.getKey();
        if (key instanceof Long)
            return keyFactory.newKey((Long) key);
        else if (key instanceof String)
            return keyFactory.newKey((String) key);
        else
            return null;
    }

    /**
     * This is the generic method which parse the query result returned from Datastore
     *
     * @param queryResults
     * @param <T>
     * @return
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     */
    protected <T> List<T> parseQueryResult(QueryResults<Entity> queryResults) throws EntityNotFoundException, InvalidEntityException {
        List<T> results = Lists.newArrayList();
        while (queryResults.hasNext()) {
            final Entity entity = queryResults.next();
            validateEntityOrThrow(entity);
            results.add(map(entity));

        }
        return results;
    }

    /**
     * This is the generic method which parse the key query result returned from Datastore
     *
     * @param queryResults
     * @param <T>
     * @return
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     */
    protected List<Key> parseKeyQueryResult(QueryResults<Key> queryResults) {
        return Lists.newArrayList(queryResults);
    }

    private T findByKey(Key key) throws InvalidEntityException {
        return map(datastore.get(key));
    }

    private void checkByKey(Key key) throws EntityNotFoundException {
        final Entity entity = datastore.get(key);
        validateEntityOrThrow(entity);
    }

    /**
     * This is the generic method which executes the entity queries and maps the results
     * @param structuredQuery<Entity>
     * @return Mapped entity list
     */
    public List<T> executeQuery(StructuredQuery<Entity> structuredQuery) {
        return parseQueryResult(datastore.run(structuredQuery));
    }

    /**
     * This is the generic method which executes the key queries and returns the key results
     * @param structuredQuery<Key>
     * @return Key list
     */
    public List<Key> executeKeyQuery(StructuredQuery<Key> structuredQuery) {
        return parseKeyQueryResult(datastore.run(structuredQuery));
    }
}
