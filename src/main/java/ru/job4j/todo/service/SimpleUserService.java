package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис, осуществляющий доступ к данным объектов модели User в репозитории
 */
@Service
@ThreadSafe
@AllArgsConstructor
public class SimpleUserService implements UserService {

    private final UserRepository repository;

    /**
     * Получить все объекты для модели User из репозитория
     *
     * @return Список пользователей. Пустой список, если ничего не найдено
     */
    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * Получить один объект User из репозитория по id
     *
     * @param id Уникальный идентификатор объекта User
     * @return Optional для объекта User, если в репозитории существует объект для переданного id.
     * Иначе -- Optional.empty()
     */
    @Override
    public Optional<User> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Получить один объект User из репозитория по значению поля login
     *
     * @param login Значение поля login объекта User
     * @return Optional для объекта User, если в репозитории существует объект для переданного значения поля login.
     * Иначе -- Optional.empty()
     */
    @Override
    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    /**
     * Получить один объект User из репозитория по значениям полей login и password
     *
     * @param login Значение поля login объекта User
     * @param password Значение поля password объекта User
     * @return Optional для объекта User, если в репозитории существует объект для
     * переданных значений полей login и password. Иначе -- Optional.empty()
     */
    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    /**
     * Добавить новый объект в репозиторий из объекта User
     *
     * @param user Объект User, который нужно добавить в репозиторий
     * @return Optional для объекта User, если удалось добавить этот объект в репозиторий. Иначе -- Optional.empty()
     */
    @Override
    public Optional<User> add(User user) {
        return repository.add(user);
    }

    /**
     * Зарегистрировать нового пользователя в системе путем добавления нового объекта в репозиторий из объекта User
     *
     * @param user Объект User, который нужно добавить в репозиторий
     * @return Optional для объекта User, если удалось добавить этот объект в репозиторий. Иначе -- Optional.empty()
     */
    @Override
    public Optional<User> register(User user) {
        return repository.add(user);
    }

    /**
     * Обновить в репозитории объект, соответствующий передаваемому объекту User
     *
     * @param user Объект User, который нужно обновить в репозитории
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(User user) {
        return repository.update(user);
    }

    /**
     * Удалить из репозитория объект, соответствующий передаваемому объекту User
     *
     * @param user Объект User, который необходимо удалить из репозитория
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean delete(User user) {
        return delete(user);
    }

    /**
     * Удалить из репозитория объект, по значению поля id объекта User
     *
     * @param id Значение поля id объекта User, который необходимо удалить из репозитория
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean deleteById(int id) {
        return false;
    }
}