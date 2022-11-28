package ru.job4j.todo.service;

import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(int id);

    Optional<User> findByLogin(String login);

    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> add(User user);

    Optional<User> register(User user);

    boolean update(User user);

    boolean delete(User user);

    boolean deleteById(int id);
}