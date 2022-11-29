package ru.job4j.todo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.todo.config.DataSourceConfig;
import ru.job4j.todo.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {DataSourceConfig.class, HibernateCrudRepository.class, HibernateUserRepository.class})
public class HibernateUserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void whenCreate() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value);
        repository.add(user);
        User userInDb = repository.findById(user.getId())
                .orElse(new User(0, null, null, null));
        assertThat(userInDb).isEqualTo(user);
        assertThat(userInDb.getLogin()).isEqualTo(userInDb.getLogin());
    }

    @Test
    public void whenUpdate() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value);
        repository.add(user);
        user.setName(user.getName() + "_updated");
        user.setLogin(user.getLogin() + "_updated");
        user.setPassword(user.getPassword() + "_updated");
        repository.update(user);
        User userInDb = repository.findById(user.getId())
                .orElse(new User(0, null, null, null));
        assertThat(userInDb).isEqualTo(user);
        assertThat(userInDb.getName()).isEqualTo(userInDb.getName());
        assertThat(userInDb.getLogin()).isEqualTo(userInDb.getLogin());
        assertThat(userInDb.getPassword()).isEqualTo(userInDb.getPassword());
    }

    @Test
    public void whenDelete()  {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value);
        repository.add(user);
        int userId = user.getId();
        repository.delete(user);
        assertThat(repository.findById(userId)).isEmpty();
    }

    @Test
    public void whenDeleteById() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value);
        repository.add(user);
        int userId = user.getId();
        repository.deleteById(userId);
        assertThat(repository.findById(userId)).isEmpty();
    }

    @Test
    public void whenFindById() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value);
        repository.add(user);
        User userInDb = repository.findById(user.getId())
                .orElse(new User(0, null, null, null));
        assertThat(userInDb).isEqualTo(user);
        assertThat(userInDb.getName()).isEqualTo(userInDb.getName());
        assertThat(userInDb.getLogin()).isEqualTo(userInDb.getLogin());
        assertThat(userInDb.getPassword()).isEqualTo(userInDb.getPassword());
    }

    @Test
    public void whenFindByLogin() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value);
        repository.add(user);
        User userInDb = repository.findByLogin(user.getLogin())
                .orElse(new User(0, null, null, null));
        assertThat(userInDb).isEqualTo(user);
        assertThat(userInDb.getName()).isEqualTo(userInDb.getName());
        assertThat(userInDb.getPassword()).isEqualTo(userInDb.getPassword());
    }

    @Test
    public void whenAddDuplicateThenEmptyOptional() {
        String value = String.valueOf(System.currentTimeMillis());
        User user = new User(0, value, value, value);
        User duplicateUser = new User(0, value, value, value);
        repository.add(user);
        Optional<User> duplicateResult = repository.add(duplicateUser);
        assertThat(duplicateResult.isEmpty()).isTrue();
    }
}