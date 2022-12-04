package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Repository
@AllArgsConstructor
public class HibernateCrudRepository implements CrudRepository {

    private final SessionFactory sf;

    @Override
    public void run(Consumer<Session> command) {
        tx(session -> {
            command.accept(session);
            return null;
        });
    }

    @Override
    public void run(String query, Map<String, Object> args) {
        Consumer<Session> command = session -> {
            var sq = session.createQuery(query);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            sq.executeUpdate();
        };
        run(command);
    }

    @Override
    public <T> List<T> query(String query, Class<T> cl) {
        Function<Session, List<T>> command = session -> session
                .createQuery(query, cl)
                .list();
        return tx(command);
    }

    @Override
    public <T> List<T> query(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            var sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.list();
        };
        return tx(command);
    }

    @Override
    public <T> List<T> query(String query, Class<T> cl, Map<String, Collection<?>> collectionArgs, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            var sq = session.createQuery(query, cl);
            for (Map.Entry<String, Collection<?>> arg : collectionArgs.entrySet()) {
                sq.setParameterList(arg.getKey(), arg.getValue());
            }
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.list();
        };
        return tx(command);
    }

    @Override
    public <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            Optional<T> result;
            var sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            result = sq.uniqueResultOptional();
            return result;
        };
        return tx(command);
    }

    @Override
    public <T> Optional<T> optional(Function<Session, T> command) {
        Optional<T> result = Optional.empty();
        try {
            result = Optional.of(tx(command));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public <T> T tx(Function<Session, T> command) {
        var session = sf
                .withOptions()
                .openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (Exception e) {
           if (tx != null) {
               tx.rollback();
           }
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean execute(Predicate<Session> command) {
        return tx(command::test);
    }

    @Override
    public boolean execute(String query, Map<String, Object> args) {
        Predicate<Session> command = session -> {
            var sq = session.createQuery(query);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.executeUpdate() > 0;
        };
        return execute(command);
    }
}