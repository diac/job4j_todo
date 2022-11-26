package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    List<Task> findAllByDone(boolean done);

    Optional<Task> findById(int id);

    Optional<Task> add(Task task);

    boolean update(Task task);

    boolean delete(Task task);

    boolean deleteById(int id);

    boolean setDescriptionById(int id, String description);

    boolean setDescription(Task task, String description);

    boolean setDoneById(int id, boolean done);

    boolean setDone(Task task, boolean done);
}