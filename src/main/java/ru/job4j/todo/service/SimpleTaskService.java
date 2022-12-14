package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.TaskRepository;
import ru.job4j.todo.util.DateAdjustment;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

    private final PriorityService priorityService;

    private final CategoryService categoryService;

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
     * Получить все объекты для модели Task из репозитория с учетом часового пояса
     *
     * @param zoneId Идентификатор часового пояса
     * @return Список задач. Пустой список, если ничего не найдено
     */
    @Override
    public List<Task> findAll(ZoneId zoneId) {
        List<Task> tasks = findAll();
        tasks.forEach(task -> task.setCreated(
                DateAdjustment.adjustByZoneId(task.getCreated(), zoneId))
        );
        return tasks;
    }

    /**
     * Получить все объекты для модели Task из репозитория, отфильтрованные по передаваемому значению done
     *
     * @param done Значение поля done для объектов Task (true -- для выполненных, false -- для невыполненных)
     * @return Список задач. Пустой список, если ничего не найдено
     */
    @Override
    public List<Task> findAllByDone(boolean done) {
        return repository.findAllByDone(done);
    }

    /**
     * Получить все объекты для модели Task из репозитория, отфильтрованные по передаваемому значению
     * с учетом часового пояса
     *
     * @param done   Значение поля done для объектов Task (true -- для выполненных, false -- для невыполненных)
     * @param zoneId Идентификатор часового пояса
     * @return Список задач. Пустой список, если ничего не найдено
     */
    @Override
    public List<Task> findAllByDone(boolean done, ZoneId zoneId) {
        List<Task> tasks = findAllByDone(done);
        tasks.forEach(task -> task.setCreated(
                DateAdjustment.adjustByZoneId(task.getCreated(), zoneId))
        );
        return tasks;
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
        return repository.findById(id);
    }

    /**
     * Получить один объект Task из репозитория по id с учетом часового пояса
     *
     * @param id     Уникальный идентификатор объекта Task
     * @param zoneId Идентификатор часового пояса
     * @return Optional для объекта Task, если в репозитории существует объект для переданного id.
     * Иначе -- Optional.empty()
     */
    @Override
    public Optional<Task> findById(int id, ZoneId zoneId) {
        Optional<Task> task = findById(id);
        task.ifPresent(foundTask -> foundTask.setCreated(
                DateAdjustment.adjustByZoneId(foundTask.getCreated(), zoneId)
        ));
        return task;
    }

    /**
     * Добавить новый объект в репозиторий из объекта Task
     *
     * @param task Объект Task, который нужно добавить в репозиторий
     * @return Optional для объекта Task, если удалось добавить этот объект в репозиторий. Иначе -- Optional.empty()
     */
    @Override
    public Optional<Task> add(Task task) {
        task.setCreated(LocalDateTime.now());
        task.setDone(false);
        return repository.add(task);
    }

    /**
     * Добавить новый объект в репозиторий из объекта Task
     *
     * @param task        Объект Task, который нужно добавить в репозиторий
     * @param priorityId  Идентификатор приоритета задачи
     * @param categoryIds Массив идентификаторов категорий задачи
     * @param user        Пользователь, которому принадлежит задача
     * @return Optional для объекта Task, если удалось добавить этот объект в репозиторий. Иначе -- Optional.empty()
     * @throws IllegalArgumentException В случае, если указан priorityId для несуществующего приоритета
     */
    @Override
    public Optional<Task> add(Task task, int priorityId, int[] categoryIds, User user) {
        task.setUser(user);
        Optional<Priority> priority = priorityService.findById(priorityId);
        if (priority.isEmpty()) {
            throw new IllegalArgumentException(String.format("Приоритет #%d не существует", priorityId));
        }
        task.setPriority(priority.get());
        task.setCategories(categoryService.findAllByIds(categoryIds));
        return add(task);
    }

    /**
     * Обновить в репозитории объект, соответствующий передаваемому объекту Task
     *
     * @param task Объект Task, который нужно обновить в репозитории
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(Task task) {
        return repository.update(task);
    }

    /**
     * Обновить в репозитории объект, соответствующий передаваемому значению идентификатора объекта Task
     *
     * @param id          Идентификатор задачи в репозитории
     * @param description Новое описание задачи
     * @param priorityId  Идентификатор приоритета задачи
     * @param categoryIds Массив идентификаторов категорий задачи
     * @return true в случае успешного обновления. Иначе -- false
     * @throws IllegalArgumentException В случае, если задача не существует для переданного id, или если
     *                                  указан priorityId для несуществующего приоритета
     */
    @Override
    public boolean update(int id, String description, int priorityId, int[] categoryIds) {
        Optional<Priority> priority = priorityService.findById(priorityId);
        if (priority.isEmpty()) {
            throw new IllegalArgumentException(String.format("Приоритет #%d не существует", priorityId));
        }
        return repository.update(id, description, priority.get(), categoryService.findAllByIds(categoryIds));
    }

    /**
     * Удалить из репозитория объект, соответствующий передаваемому объекту Task
     *
     * @param task Объект Task, который необходимо удалить из репозитория
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean delete(Task task) {
        return repository.delete(task);
    }

    /**
     * Удалить из репозитория объект, по значению поля id объекта Task
     *
     * @param id Значение поля id объекта Task, который необходимо удалить из репозитория
     * @return true в случае успешного удаления. Иначе -- false
     */
    @Override
    public boolean deleteById(int id) {
        return repository.deleteById(id);
    }

    /**
     * Обновить значение поля description объекта Task по id
     *
     * @param id          Значение поля id объекта Task
     * @param description Новое значение поля description
     * @return true в случае успешного обновления поля. Иначе -- false
     */
    @Override
    public boolean updateDescriptionById(int id, String description) {
        return repository.setDescriptionById(id, description);
    }

    /**
     * Отметить задачу Task как выполненную и сохранить соответствующий объект в репозитории
     *
     * @param task Задача, которая будет помечена как выполненная
     * @return true в случае успешного обновления поля. Иначе -- false
     */
    @Override
    public boolean complete(Task task) {
        return repository.setDone(task, true);
    }

    /**
     * Отметить задачу Task как выполненную по id и сохранить соответствующий объект в репозитории
     *
     * @param id Значение поля id для объекта Task
     * @return true в случае успешного обновления поля. Иначе -- false
     */
    @Override
    public boolean completeById(int id) {
        return repository.setDoneById(id, true);
    }
}