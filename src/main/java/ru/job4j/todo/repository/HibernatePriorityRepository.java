package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Репозиторий, отвечающий за сериализацию/десериализацию объектов модели Priority в БД
 */
@Repository
@AllArgsConstructor
public class HibernatePriorityRepository implements PriorityRepository {

    private static final String FIND_ALL_QUERY = "SELECT p FROM Priority p";

    private static final String FIND_BY_ID_QUERY = "SELECT p FROM Priority p WHERE id = :fId";

    private static final String DELETE_QUERY = "DELETE FROM Priority WHERE id = :fId";

    private final CrudRepository crudRepository;

    /**
     * Получить все записи для модели Priority из БД
     *
     * @return Список задач. Пустой список, если ничего не найдено
     */
    @Override
    public List<Priority> findAll() {
        return crudRepository.query(FIND_ALL_QUERY, Priority.class);
    }

    /**
     * Получить один объект Priority из БД по id
     *
     * @param id Уникальный идентификатор объекта Priority
     * @return Optional для объекта Priority, если в БД существует запись для переданного id. Иначе -- Optional.empty()
     */
    @Override
    public Optional<Priority> findById(int id) {
        return crudRepository.optional(
                FIND_BY_ID_QUERY,
                Priority.class,
                Map.of("fId", id)
        );
    }

    /**
     * Добавить новую запись в БД из объекта Priority
     *
     * @param priority Объект Priority из которого создается новая запись в БД
     * @return Optional объекта Priority, соответствующего новой созданной записи в БД.
     * Optional.empty() в случае, если новую запись не удалось создать (напр., из-за нарушения
     * ссылочной целостности)
     */
    @Override
    public Optional<Priority> add(Priority priority) {
        return crudRepository.optional(session -> {
            session.persist(priority);
            return priority;
        });
    }

    /**
     * Обновить в БД запись, соответсвующую передаваемому объекту Priority
     *
     * @param priority Объект Priority, для которого необходимо обновить запись в БД
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(Priority priority) {
        return crudRepository.execute(session -> {
            session.merge(priority);
            return true;
        });
    }

    /**
     * Удалить из БД запись, соответствующую передаваемому объекту Priority
     *
     * @param priority Объект Priority, для которого необходимо удалить запись из БД
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean delete(Priority priority) {
        return crudRepository.execute(DELETE_QUERY, Map.of("fId", priority.getId()));
    }

    /**
     * Удалить из БД запись, по id объекта Priority в репозитории
     *
     * @param id Идентификатор объекта Priority, для которого необходимо удалить запись из БД
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean deleteById(int id) {
        return crudRepository.execute(DELETE_QUERY, Map.of("fId", id));
    }
}