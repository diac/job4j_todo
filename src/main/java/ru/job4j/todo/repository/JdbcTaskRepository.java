package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Репозиторий, отвечающий за сериализацию/десериализацию объектов модели Task в БД
 */
@Repository
@ThreadSafe
@AllArgsConstructor
public class JdbcTaskRepository implements TaskRepository {

    private static final String FIND_ALL_QUERY = "SELECT t FROM Task t";

    private static final String FIND_ALL_BY_DONE_QUERY = "SELECT t FROM Task t WHERE done = :fDone";

    private static final String FIND_BY_ID_QUERY = "SELECT t FROM Task t WHERE id = :fId";

    private static final String UPDATE_QUERY = """
            UPDATE
                Task
            SET
                description = :fDescription,
                created = :fCreated,
                done = :fDone
            WHERE
                id = :fId""";

    private static final String DELETE_QUERY = "DELETE FROM Task WHERE id = :fId";

    private final SessionFactory sf;

    /**
     * Получить все записи для модели Task из БД
     *
     * @return Список задач. Пустой список, если ничего не найдено
     */
    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<Task> query = session.createQuery(FIND_ALL_QUERY);
                tasks = query.getResultList();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return tasks;
    }

    /**
     * Получить все записи для модели Task из БД, отфильтрованные по передаваемому значению done
     *
     * @param done Значение поля done для объектов Task (true -- для выполненных, false -- для невыполненных)
     * @return Список задач. Пустой список, если ничего не найдено
     */
    @Override
    public List<Task> findAllByDone(boolean done) {
        List<Task> tasks = new ArrayList<>();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<Task> query = session.createQuery(FIND_ALL_BY_DONE_QUERY)
                        .setParameter("fDone", done);
                tasks = query.getResultList();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return tasks;
    }

    /**
     * Получить один объект Task из БД по id
     *
     * @param id Уникальный идентификатор объекта Task
     * @return Optional для объекта Task, если в БД существует запись для переданного id. Иначе -- Optional.empty()
     */
    @Override
    public Optional<Task> findById(int id) {
        Optional<Task> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<Task> query = session.createQuery(FIND_BY_ID_QUERY)
                        .setParameter("fId", id);
                result = Optional.of(query.getSingleResult());
                session.getTransaction().commit();
            } catch (NoResultException nre) {
                result = Optional.empty();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Добавить новую запись в БД из объекта Task
     *
     * @param task Объект Task из которого создается новая запись в БД
     * @return Optional оъекта Task, соответствующего новой созданной записи в БД.
     * Optional.empty() в случае, если новую запись не удалось создать (напр., из-за нарушения
     * ссылочной целостности)
     */
    @Override
    public Optional<Task> add(Task task) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.persist(task);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return Optional.of(task);
    }

    /**
     * Обновить в БД запись, соотвутетсвующую передаваемому объекту Task
     *
     * @param task Объект Task, для которого необходимо обновить запись в БД
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(Task task) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query query = session.createQuery(UPDATE_QUERY)
                        .setParameter("fId", task.getId())
                        .setParameter("fDescription", task.getDescription())
                        .setParameter("fCreated", task.getCreated())
                        .setParameter("fDone", task.isDone());
                result = query.executeUpdate() > 0;
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Удалить из БД запись, соответствующую передаваемому объекту Task
     *
     * @param task Объект Task, для которого необходимо удалить запись из БД
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean delete(Task task) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query query = session.createQuery(DELETE_QUERY)
                        .setParameter("fId", task.getId());
                result = query.executeUpdate() > 0;
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }
}