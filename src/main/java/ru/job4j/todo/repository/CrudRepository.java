package ru.job4j.todo.repository;

import org.hibernate.Session;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface CrudRepository {

    void run(Consumer<Session> command);

    void run(String query, Map<String, Object> args);

    <T> List<T> query(String query, Class<T> cl);

    <T> List<T> query(String query, Class<T> cl, Map<String, Object> args);

    <T> List<T> query(String query, Class<T> cl, Map<String, Collection<?>> collectionArgs, Map<String, Object> args);

    <T> Optional<T> optional(Function<Session, T> command);

    <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> args);

    <T> T tx(Function<Session, T> command);

    boolean execute(Predicate<Session> command);

    boolean execute(String query, Map<String, Object> args);
}