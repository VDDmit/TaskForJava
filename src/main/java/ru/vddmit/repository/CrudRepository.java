package ru.vddmit.repository;

import java.util.List;

public interface CrudRepository<T> {
    default void save(T entity) {
        throw new UnsupportedOperationException("Operation not supported");
    }
    default void update(T entity) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    default void delete(Long id) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    default T findById(Long id) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    default List<T> findAll() {
        throw new UnsupportedOperationException("Operation not supported");
    }


}
