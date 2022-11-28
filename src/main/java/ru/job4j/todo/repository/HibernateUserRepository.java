package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.ArrayList;
import java.util.List;
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

    private static final String UPDATE_QUERY = """
            UPDATE
                User
            SET
                name = :fName,
                login = :fLogin,
                password = :fPassword
            WHERE
                id = :fId
            """;

    private static final String DELETE_QUERY = "DELETE FROM User WHERE id = :fId";

    private final SessionFactory sf;

    /**
     * Получить все записи для модели User из БД
     *
     * @return Список пользователей. Пустой список, если ничего не найдено
     */
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<User> query = session.createQuery(FIND_ALL_QUERY);
                users = query.getResultList();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return users;
    }

    /**
     * Получить один объект User из БД по id
     *
     * @param id Уникальный идентификатор объекта User
     * @return Optional для объекта User, если в БД существует запись для переданного id. Иначе -- Optional.empty()
     */
    @Override
    public Optional<User> findById(int id) {
        Optional<User> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<User> query = session.createQuery(FIND_BY_ID_QUERY)
                        .setParameter("fId", id);
                result = query.uniqueResultOptional();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return result;
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
        Optional<User> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<User> query = session.createQuery(FIND_BY_LOGIN_QUERY)
                        .setParameter("fLogin", login);
                result = query.uniqueResultOptional();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return result;
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
        Optional<User> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<User> query = session.createQuery(FIND_BY_LOGIN_AND_PASSWORD_QUERY)
                        .setParameter("fLogin", login)
                        .setParameter("fPassword", password);
                result = query.uniqueResultOptional();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return result;
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
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.persist(user);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return Optional.of(user);
    }

    /**
     * Обновить в БД запись, соответсвующую передаваемому объекту User
     *
     * @param user Объект User, для которого необходимо обновить запись в БД
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(User user) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query query = session.createQuery(UPDATE_QUERY)
                        .setParameter("fId", user.getId())
                        .setParameter("fName", user.getName())
                        .setParameter("fLogin", user.getLogin())
                        .setParameter("fPassword", user.getPassword());
                result = query.executeUpdate() > 0;
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Удалить из БД запись, соответствующую передаваемому объекту User
     *
     * @param user Объект User, для которого необходимо удалить запись из БД
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean delete(User user) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query query = session.createQuery(DELETE_QUERY)
                        .setParameter("fId", user.getId());
                result = query.executeUpdate() > 0;
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Удалить из БД запись, по id объекта User в репозитории
     *
     * @param id Идентификатор объекта User, для которого необходимо удалить запись из БД
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean deleteById(int id) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query query = session.createQuery(DELETE_QUERY)
                        .setParameter("fId", id);
                result = query.executeUpdate() > 0;
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }
}