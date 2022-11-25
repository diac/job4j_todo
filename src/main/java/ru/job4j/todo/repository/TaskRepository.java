package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    Optional<Task> findById(int id);

    Optional<Task> add(Task task);

    boolean update(Task task);

    boolean delete(Task task);
}