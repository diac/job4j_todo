package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Репозиторий, отвечающий за сериализацию/десериализацию объектов модели Task в БД
 */
@Repository
@ThreadSafe
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {

    private static final String FIND_ALL_QUERY = "SELECT t FROM Task t JOIN FETCH t.priority";

    private static final String FIND_ALL_BY_DONE_QUERY
            = "SELECT t FROM Task t JOIN FETCH t.priority WHERE done = :fDone";

    private static final String FIND_BY_ID_QUERY = "SELECT t FROM Task t JOIN FETCH t.priority WHERE t.id = :fId";

    private static final String UPDATE_DESCRIPTION_BY_ID_QUERY
            = "UPDATE Task SET description = :fDescription WHERE id = :fId";

    private static final String UPDATE_DONE_BY_ID_QUERY = "UPDATE Task SET done = :fDone WHERE id = :fId";

    private static final String DELETE_QUERY = "DELETE FROM Task WHERE id = :fId";

    private final CrudRepository crudRepository;

    /**
     * Получить все записи для модели Task из БД
     *
     * @return Список задач. Пустой список, если ничего не найдено
     */
    @Override
    public List<Task> findAll() {
        return crudRepository.query(FIND_ALL_QUERY, Task.class);
    }

    /**
     * Получить все записи для модели Task из БД, отфильтрованные по передаваемому значению done
     *
     * @param done Значение поля done для объектов Task (true -- для выполненных, false -- для невыполненных)
     * @return Список задач. Пустой список, если ничего не найдено
     */
    @Override
    public List<Task> findAllByDone(boolean done) {
        return crudRepository.query(
                FIND_ALL_BY_DONE_QUERY,
                Task.class,
                Map.of("fDone", done)
        );
    }

    /**
     * Получить один объект Task из БД по id
     *
     * @param id Уникальный идентификатор объекта Task
     * @return Optional для объекта Task, если в БД существует запись для переданного id. Иначе -- Optional.empty()
     */
    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                FIND_BY_ID_QUERY,
                Task.class,
                Map.of("fId", id)
        );
    }

    /**
     * Добавить новую запись в БД из объекта Task
     *
     * @param task Объект Task из которого создается новая запись в БД
     * @return Optional объекта Task, соответствующего новой созданной записи в БД.
     * Optional.empty() в случае, если новую запись не удалось создать (напр., из-за нарушения
     * ссылочной целостности)
     */
    @Override
    public Optional<Task> add(Task task) {
        return crudRepository.optional(session -> {
            session.persist(task);
            return task;
        });
    }

    /**
     * Обновить в БД запись, соответсвующую передаваемому объекту Task
     *
     * @param task Объект Task, для которого необходимо обновить запись в БД
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(Task task) {
        return crudRepository.execute(session -> {
            session.merge(task);
            return true;
        });
    }

    /**
     * Удалить из БД запись, соответствующую передаваемому объекту Task
     *
     * @param task Объект Task, для которого необходимо удалить запись из БД
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean delete(Task task) {
        return crudRepository.execute(DELETE_QUERY, Map.of("fId", task.getId()));
    }

    /**
     * Удалить из БД запись, по id объекта Task в репозитории
     *
     * @param id Идентификатор объекта Task, для которого необходимо удалить запись из БД
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean deleteById(int id) {
        return crudRepository.execute(DELETE_QUERY, Map.of("fId", id));
    }

    /**
     * Обновить значение поля description для объекта Task в репозитории по id
     *
     * @param id          Значение поля id объекта Task, для которого нужно обновить поле description
     * @param description Новое значение поля description
     * @return true в случае успешного обновления, иначе -- false
     */
    @Override
    public boolean setDescriptionById(int id, String description) {
        return crudRepository.execute(
                UPDATE_DESCRIPTION_BY_ID_QUERY,
                Map.of(
                        "fId", id,
                        "fDescription", description
                )
        );
    }

    /**
     * Обновить значение поля description для объекта Task
     *
     * @param task        Объект Task, для которого нужно обновить поле description
     * @param description Новое значение поля description
     * @return true в случае успешного обновления, иначе -- false
     */
    @Override
    public boolean setDescription(Task task, String description) {
        return crudRepository.execute(
                UPDATE_DESCRIPTION_BY_ID_QUERY,
                Map.of(
                        "fId", task.getId(),
                        "fDescription", description
                )
        );
    }

    /**
     * Обновить значение поля done для объекта Task в репозитории по id
     *
     * @param id   Значение поля id объекта Task, для которого нужно обновить поле done
     * @param done Новое значение поля done
     * @return true в случае успешного обновления, иначе -- false
     */
    @Override
    public boolean setDoneById(int id, boolean done) {
        return crudRepository.execute(
                UPDATE_DONE_BY_ID_QUERY,
                Map.of(
                        "fId", id,
                        "fDone", done
                )
        );
    }

    /**
     * Обновить значение поля done для объекта Task
     *
     * @param task Объект Task, для которого нужно обновить поле done
     * @param done Новое значение поля done
     * @return true в случае успешного обновления, иначе -- false
     */
    public boolean setDone(Task task, boolean done) {
        return crudRepository.execute(
                UPDATE_DONE_BY_ID_QUERY,
                Map.of(
                        "fId", task.getId(),
                        "fDone", done
                )
        );
    }
}