package ru.job4j.todo.service;

import ru.job4j.todo.dto.TaskFormDto;
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

    Optional<Task> add(TaskFormDto taskFormDto);

    boolean update(Task task);

    boolean update(int id, TaskFormDto taskFormDto);

    boolean delete(Task task);

    boolean deleteById(int id);

    boolean updateDescriptionById(int id, String description);

    boolean complete(Task task);

    boolean completeById(int id);

    Task fromDto(TaskFormDto taskFormDto);

    TaskFormDto toDto(Task task);
}