package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис, осуществляющий доступ к данным объектов модели Task в репозитории
 */
@Service
@ThreadSafe
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

    private final TaskRepository repository;

    /**
     * Получить все объекты для модели Task из репозитория
     *
     * @return Список задач. Пустой список, если ничего не найдено
     */
    @Override
    public List<Task> findAll() {
        return repository.findAll();
    }

    /**
     * Получить все объекты для модели Task из репозитория, отфильтрованные по передаваемому значению done
     *
     * @param done Значение поля done для объектов Task (true -- для выполненных, false -- для невыполненных)
     * @return Список задач. Пустой список, если ничего не найдено
     */
    @Override
    public List<Task> findAllByDone(boolean done) {
        return null;
    }

    /**
     * Получить один объект Task из репозитория по id
     *
     * @param id Уникальный идентификатор объекта Task
     * @return Optional для объекта Task, если в репозитории существует объект для переданного id.
     * Иначе -- Optional.empty()
     */
    @Override
    public Optional<Task> findById(int id) {
        return Optional.empty();
    }

    /**
     * Добавить новый объект в репозиторий из объекта Task
     *
     * @param task Объект Task, который нужно добавить в репозиторий
     * @return Optional для объекта Task, если удалось добавить этот объект в репозиторий. Иначе -- Optional.empty()
     */
    @Override
    public Optional<Task> add(Task task) {
        return Optional.empty();
    }

    /**
     * Обновить в репозитории объект, соответствующий передаваемому объекту Task
     *
     * @param task Объект Task, который нужно обновить в репозитории
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(Task task) {
        return false;
    }

    /**
     * Удалить из репозитория объект, соответствующий передаваемому объекту Task
     *
     * @param task Объект Task, который необходимо удалить из репозитория
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean delete(Task task) {
        return false;
    }
}