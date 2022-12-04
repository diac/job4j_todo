package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> findAll();

    List<Task> findAll(ZoneId zoneId);

    List<Task> findAllByDone(boolean done);

    List<Task> findAllByDone(boolean done, ZoneId zoneId);

    Optional<Task> findById(int id);

    Optional<Task> findById(int id, ZoneId zoneId);

    Optional<Task> add(Task task);

    Optional<Task> add(Task task, int priorityId, int[] categoryIds, User user);

    boolean update(Task task);

    boolean update(Task task, int id, int priorityId, int[] categoryIds);

    boolean delete(Task task);

    boolean deleteById(int id);

    boolean updateDescriptionById(int id, String description);

    boolean complete(Task task);

    boolean completeById(int id);
}