package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Репозиторий, отвечающий за сериализацию/десериализацию объектов модели User в БД
 */
@Repository
@ThreadSafe
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private static final String FIND_ALL_QUERY = "SELECT u FROM User u";

    private static final String FIND_BY_ID_QUERY = "SELECT u FROM User u WHERE id = :fId";

    private static final String FIND_BY_LOGIN_QUERY = "SELECT u FROM User u WHERE login = :fLogin";

    private static final String FIND_BY_LOGIN_AND_PASSWORD_QUERY
            = "SELECT u FROM User u WHERE login = :fLogin AND password = :fPassword";

    private static final String DELETE_QUERY = "DELETE FROM User WHERE id = :fId";

    private final CrudRepository crudRepository;

    /**
     * Получить все записи для модели User из БД
     *
     * @return Список пользователей. Пустой список, если ничего не найдено
     */
    @Override
    public List<User> findAll() {
        return crudRepository.query(FIND_ALL_QUERY, User.class);
    }

    /**
     * Получить один объект User из БД по id
     *
     * @param id Уникальный идентификатор объекта User
     * @return Optional для объекта User, если в БД существует запись для переданного id. Иначе -- Optional.empty()
     */
    @Override
    public Optional<User> findById(int id) {
        return crudRepository.optional(
                FIND_BY_ID_QUERY,
                User.class,
                Map.of("fId", id)
        );
    }

    /**
     * Получить один объект User из БД по значению поля login
     *
     * @param login Логин пользователя в системе
     * @return Optional для объекта User, если в БД существует запись для переданного значения login.
     * Иначе -- Optional.empty()
     */
    @Override
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                FIND_BY_LOGIN_QUERY,
                User.class,
                Map.of("fLogin", login)
        );
    }

    /**
     * Получить один объект User из БД по значению полей login и password
     *
     * @param login    Логин пользователя в системе
     * @param password Пароль пользователя в системе
     * @return Optional для объекта User, если в БД существует запись для переданных значений login и password.
     * Иначе -- Optional.empty()
     */
    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                FIND_BY_LOGIN_AND_PASSWORD_QUERY,
                User.class,
                Map.of(
                        "fLogin", login,
                        "fPassword", password
                )
        );
    }

    /**
     * Добавить новую запись в БД из объекта User
     *
     * @param user Объект User из которого создается новая запись в БД
     * @return Optional объекта User, соответствующего новой созданной записи в БД.
     * Optional.empty() в случае, если новую запись не удалось создать (напр., из-за нарушения
     * ссылочной целостности)
     */
    @Override
    public Optional<User> add(User user) {
        return crudRepository.optional(session -> {
            session.persist(user);
            return user;
        });
    }

    /**
     * Обновить в БД запись, соответсвующую передаваемому объекту User
     *
     * @param user Объект User, для которого необходимо обновить запись в БД
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(User user) {
        return crudRepository.execute(session -> {
            session.merge(user);
            return true;
        });
    }

    /**
     * Удалить из БД запись, соответствующую передаваемому объекту User
     *
     * @param user Объект User, для которого необходимо удалить запись из БД
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean delete(User user) {
        return crudRepository.execute(DELETE_QUERY, Map.of("fId", user.getId()));
    }

    /**
     * Удалить из БД запись, по id объекта User в репозитории
     *
     * @param id Идентификатор объекта User, для которого необходимо удалить запись из БД
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean deleteById(int id) {
        return crudRepository.execute(DELETE_QUERY, Map.of("fId", id));
    }
}