package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.*;

/**
 * Репозиторий, отвечающий за сериализацию/десериализацию объектов модели Category в БД
 */
@Repository
@AllArgsConstructor
public class HibernateCategoryRepository implements CategoryRepository {

    private static final String FIND_ALL_QUERY = "SELECT c FROM Category c";

    private static final String FIND_BY_ID_QUERY = "SELECT c FROM Category c WhERE c.id = :fId";

    private static final String DELETE_QUERY = "DELETE FROM Category WHERE id = :fId";

    private static final String FIND_ALL_BY_IDS_QUERY = "SELECT c FROM Category c WHERE id IN (:fIds)";

    private final CrudRepository crudRepository;

    /**
     * Получить все записи для модели Category из БД
     *
     * @return Список категорий. Пустой список, если ничего не найдено
     */
    @Override
    public List<Category> findAll() {
        return crudRepository.query(FIND_ALL_QUERY, Category.class);
    }

    /**
     * Получить один объект Category из БД по id
     *
     * @param id Уникальный идентификатор объекта Category
     * @return Optional для объекта Category, если в БД существует запись для переданного id. Иначе -- Optional.empty()
     */
    @Override
    public Optional<Category> findById(int id) {
        return crudRepository.optional(
                FIND_BY_ID_QUERY,
                Category.class,
                Map.of("fId", id)
        );
    }

    /**
     * Добавить новую запись в БД из объекта Category
     *
     * @param category Объект Category из которого создается новая запись в БД
     * @return Optional объекта Category, соответствующего новой созданной записи в БД.
     * Optional.empty() в случае, если новую запись не удалось создать (напр., из-за нарушения
     * ссылочной целостности)
     */
    @Override
    public Optional<Category> add(Category category) {
        return crudRepository.optional(session -> {
            session.persist(category);
            return category;
        });
    }

    /**
     * Обновить в БД запись, соответсвующую передаваемому объекту Category
     *
     * @param category Объект Category, для которого необходимо обновить запись в БД
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(Category category) {
        return crudRepository.execute(session -> {
            session.merge(category);
            return true;
        });
    }

    /**
     * Удалить из БД запись, соответствующую передаваемому объекту Category
     *
     * @param category Объект Category, для которого необходимо удалить запись из БД
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean delete(Category category) {
        return crudRepository.execute(DELETE_QUERY, Map.of("fId", category.getId()));
    }

    /**
     * Удалить из БД запись, по id объекта Category в репозитории
     *
     * @param id Идентификатор объекта Category, для которого необходимо удалить запись из БД
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean deleteById(int id) {
        return crudRepository.execute(DELETE_QUERY, Map.of("fId", id));
    }

    /**
     * Найти все категории по передаваемому массиву идентификаторов
     *
     * @param ids Массив идентификаторов объектов Category
     * @return Найденный набор категорий
     */
    @Override
    public Set<Category> findAllByIds(int[] ids) {
        return new HashSet<>(
                crudRepository.query(
                        FIND_ALL_BY_IDS_QUERY,
                        Category.class,
                        Map.of("fIds", Arrays.stream(ids).boxed().toList()),
                        Map.of()
                )
        );
    }
}