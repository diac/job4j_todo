package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> findAll();

    List<Task> findAll(ZoneId zoneId);

    List<Task> findAllByDone(boolean done);

    Optional<Task> findById(int id);

    Optional<Task> add(Task task);

    boolean update(Task task);

    boolean delete(Task task);

    boolean deleteById(int id);

    boolean updateDescriptionById(int id, String description);

    boolean complete(Task task);

    boolean completeById(int id);
}