package ru.job4j.todo.repository;

import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(int id);

    Optional<User> findByLogin(String login);

    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> add(User user);

    boolean update(User user);

    boolean delete(User user);

    boolean deleteById(int id);
}