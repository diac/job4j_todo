package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис, осуществляющий доступ к данным объектов модели Category в репозитории
 */
@Service
@AllArgsConstructor
public class SimpleCategoryService implements CategoryService {

    private final CategoryRepository repository;

    /**
     * Получить все объекты для модели Category из репозитория
     *
     * @return Список категорий. Пустой список, если ничего не найдено
     */
    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    /**
     * Получить один объект Category из репозитория по id
     *
     * @param id Уникальный идентификатор объекта Category
     * @return Optional для объекта Category, если в репозитории существует объект для переданного id.
     * Иначе -- Optional.empty()
     */
    @Override
    public Optional<Category> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Добавить новый объект в репозиторий из объекта Category
     *
     * @param category Объект Category, который нужно добавить в репозиторий
     * @return Optional для объекта Category, если удалось добавить этот объект в репозиторий. Иначе -- Optional.empty()
     */
    @Override
    public Optional<Category> add(Category category) {
        return repository.add(category);
    }

    /**
     * Обновить в репозитории объект, соответствующий передаваемому объекту Category
     *
     * @param category Объект Category, который нужно обновить в репозитории
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(Category category) {
        return repository.update(category);
    }

    /**
     * Удалить из репозитория объект, соответствующий передаваемому объекту Category
     *
     * @param category Объект Category, который необходимо удалить из репозитория
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean delete(Category category) {
        return repository.delete(category);
    }

    /**
     * Удалить из репозитория объект, по значению поля id объекта Category
     *
     * @param id Значение поля id объекта Category, который необходимо удалить из репозитория
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean deleteById(int id) {
        return repository.deleteById(id);
    }
}