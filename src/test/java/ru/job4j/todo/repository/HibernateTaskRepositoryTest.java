package ru.job4j.todo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.todo.Main;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {Main.class, HibernateTaskRepository.class})
public class HibernateTaskRepositoryTest {

    @Autowired
    private TaskRepository repository;

    @Test
    public void whenCreateTask() {
        String taskDescription = String.valueOf(System.currentTimeMillis());
        Task task = new Task(0, taskDescription, LocalDateTime.now(), false);
        repository.add(task);
        Task taskInDb = repository.findById(task.getId())
                .orElse(new Task(0, null, LocalDateTime.now(), false));
        assertThat(taskInDb.getId()).isEqualTo(task.getId());
        assertThat(taskInDb.getDescription()).isEqualTo(task.getDescription());
    }

    @Test
    public void whenUpdateTask() {
        String taskDescription = String.valueOf(System.currentTimeMillis());
        Task task = new Task(0, taskDescription, LocalDateTime.now(), false);
        repository.add(task);
        task.setDescription(task.getDescription() + "_updated");
        task.setDone(true);
        repository.update(task);
        Task taskInDb = repository.findById(task.getId())
                .orElse(new Task(0, null, LocalDateTime.now(), false));
        assertThat(taskInDb.getId()).isEqualTo(task.getId());
        assertThat(taskInDb.getDescription()).isEqualTo(task.getDescription());
        assertThat(taskInDb.isDone()).isEqualTo(task.isDone());
    }

    @Test
    public void whenDeleteTask() {
        String taskDescription = String.valueOf(System.currentTimeMillis());
        Task task = new Task(0, taskDescription, LocalDateTime.now(), false);
        repository.add(task);
        int taskId = task.getId();
        repository.delete(task);
        assertThat(repository.findById(taskId)).isEmpty();
    }

    @Test
    public void whenFindAllByDone() {
        String taskDescription = String.valueOf(System.currentTimeMillis());
        Task task = new Task(0, taskDescription, LocalDateTime.now(), true);
        repository.add(task);
        List<Task> tasks = repository.findAllByDone(true);
        System.out.println(tasks);
        System.out.println(tasks.contains(task));
        assertThat(tasks.contains(task)).isTrue();
    }
}