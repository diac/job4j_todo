package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.PriorityRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис, осуществляющий доступ к данным объектов модели Priority в репозитории
 */
@Service
@AllArgsConstructor
public class SimplePriorityService implements PriorityService {

    private PriorityRepository repository;

    /**
     * Получить все объекты для модели Priority из репозитория
     *
     * @return Список приоритетов. Пустой список, если ничего не найдено
     */
    @Override
    public List<Priority> findAll() {
        return repository.findAll();
    }

    /**
     * Получить один объект Priority из репозитория по id
     *
     * @param id Уникальный идентификатор объекта Priority
     * @return Optional для объекта Priority, если в репозитории существует объект для переданного id.
     * Иначе -- Optional.empty()
     */
    @Override
    public Optional<Priority> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Добавить новый объект в репозиторий из объекта Priority
     *
     * @param priority Объект Priority который нужно добавить в репозиторий
     * @return Optional для объекта Priority, если удалось добавить этот объект в репозиторий. Иначе -- Optional.empty()
     */
    @Override
    public Optional<Priority> add(Priority priority) {
        return repository.add(priority);
    }

    /**
     * Обновить в репозитории объект, соответствующий передаваемому объекту Priority
     *
     * @param priority Объект Priority, который нужно обновить в репозитории
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(Priority priority) {
        return repository.update(priority);
    }

    /**
     * Удалить из репозитория объект, соответствующий передаваемому объекту Priority
     *
     * @param priority Объект Priority, который необходимо удалить из репозитория
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean delete(Priority priority) {
        return repository.delete(priority);
    }

    /**
     * Удалить из репозитория объект, по значению поля id объекта Priority
     *
     * @param id Значение поля id объекта Priority, который необходимо удалить из репозитория
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean deleteById(int id) {
        return repository.deleteById(id);
    }
}