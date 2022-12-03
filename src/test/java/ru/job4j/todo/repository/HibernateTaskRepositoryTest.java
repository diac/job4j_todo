package ru.job4j.todo.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.todo.config.DataSourceConfig;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@SpringBootTest(classes = {
        DataSourceConfig.class,
        HibernateCrudRepository.class,
        HibernateTaskRepository.class,
        HibernateUserRepository.class
})
public class HibernateTaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenCreateTask() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value, TimeZone.getDefault().toZoneId().toString());
        userRepository.add(user);
        Task task = new Task(0, value, LocalDateTime.now(), false, user, null, new HashSet<>());
        taskRepository.add(task);
        Task taskInDb = taskRepository.findById(task.getId())
                .orElse(new Task(0, null, LocalDateTime.now(), false, null, null, new HashSet<>()));
        assertThat(taskInDb.getId()).isEqualTo(task.getId());
        assertThat(taskInDb.getDescription()).isEqualTo(task.getDescription());
    }

    @Test
    public void whenUpdateTask() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value, TimeZone.getDefault().toZoneId().toString());
        userRepository.add(user);
        Task task = new Task(0, value, LocalDateTime.now(), false, user, null, new HashSet<>());
        taskRepository.add(task);
        task.setDescription(task.getDescription() + "_updated");
        task.setDone(true);
        taskRepository.update(task);
        Task taskInDb = taskRepository.findById(task.getId())
                .orElse(new Task(0, null, LocalDateTime.now(), false, null, null, new HashSet<>()));
        assertThat(taskInDb.getId()).isEqualTo(task.getId());
        assertThat(taskInDb.getDescription()).isEqualTo(task.getDescription());
        assertThat(taskInDb.isDone()).isEqualTo(task.isDone());
    }

    @Test
    public void whenDeleteTask() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value, TimeZone.getDefault().toZoneId().toString());
        userRepository.add(user);
        Task task = new Task(0, value, LocalDateTime.now(), false, user, null, new HashSet<>());
        taskRepository.add(task);
        int taskId = task.getId();
        taskRepository.delete(task);
        assertThat(taskRepository.findById(taskId)).isEmpty();
    }

    @Test
    public void whenDeleteById() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value, TimeZone.getDefault().toZoneId().toString());
        userRepository.add(user);
        Task task = new Task(0, value, LocalDateTime.now(), false, user, null, new HashSet<>());
        taskRepository.add(task);
        int taskId = task.getId();
        taskRepository.deleteById(task.getId());
        assertThat(taskRepository.findById(taskId)).isEmpty();
    }

    @Test
    public void whenFindAllByDone() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value, TimeZone.getDefault().toZoneId().toString());
        userRepository.add(user);
        Task task = new Task(0, value, LocalDateTime.now(), true, user, null, new HashSet<>());
        taskRepository.add(task);
        List<Task> tasks = taskRepository.findAllByDone(true);
        assertThat(tasks.contains(task)).isTrue();
    }

    @Test
    public void whenSetDescriptionById() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value, TimeZone.getDefault().toZoneId().toString());
        userRepository.add(user);
        Task task = new Task(0, "test", LocalDateTime.now(), true, user, null, new HashSet<>());
        taskRepository.add(task);
        taskRepository.setDescriptionById(task.getId(), task.getDescription() + "_updated");
        Task taskInDb = taskRepository.findById(task.getId())
                .orElse(new Task(0, null, LocalDateTime.now(), false, null, null, new HashSet<>()));
        assertThat(task).isEqualTo(taskInDb);
        assertThat("test_updated").isEqualTo(taskInDb.getDescription());
    }

    @Test
    public void whenSetDescription() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value, TimeZone.getDefault().toZoneId().toString());
        userRepository.add(user);
        Task task = new Task(0, "test", LocalDateTime.now(), true, user, null, new HashSet<>());
        taskRepository.add(task);
        taskRepository.setDescription(task, task.getDescription() + "_updated");
        Task taskInDb = taskRepository.findById(task.getId())
                .orElse(new Task(0, null, LocalDateTime.now(), false, null, null, new HashSet<>()));
        assertThat(task).isEqualTo(taskInDb);
        assertThat("test_updated").isEqualTo(taskInDb.getDescription());
    }

    @Test
    public void whenSetDone() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value, TimeZone.getDefault().toZoneId().toString());
        userRepository.add(user);
        Task task = new Task(0, null, LocalDateTime.now(), true, user, null, new HashSet<>());
        taskRepository.add(task);
        boolean done = task.isDone();
        taskRepository.setDone(task, false);
        Task taskInDb = taskRepository.findById(task.getId())
                .orElse(new Task(0, null, LocalDateTime.now(), false, null, null, new HashSet<>()));
        assertThat(task).isEqualTo(taskInDb);
        assertThat(done).isNotEqualTo(taskInDb.isDone());
    }

    @Test
    public void whenSetDoneById() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value, TimeZone.getDefault().toZoneId().toString());
        userRepository.add(user);
        Task task = new Task(0, null, LocalDateTime.now(), true, user, null, new HashSet<>());
        taskRepository.add(task);
        boolean done = task.isDone();
        taskRepository.setDoneById(task.getId(), false);
        Task taskInDb = taskRepository.findById(task.getId())
                .orElse(new Task(0, null, LocalDateTime.now(), false, null, null, new HashSet<>()));
        assertThat(task).isEqualTo(taskInDb);
        assertThat(done).isNotEqualTo(taskInDb.isDone());
    }
}