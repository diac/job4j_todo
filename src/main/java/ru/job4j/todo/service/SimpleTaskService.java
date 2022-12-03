package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.TaskFormDto;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.*;

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
     * Добавить новый объект в репозиторий из объекта TaskFormDto
     *
     * @param taskFormDto Объект TaskFormDto, из которого будет создан и добавлен в репозиторий новый объект Task
     * @return Optional для объекта Task, если удалось добавить этот объект в репозиторий. Иначе -- Optional.empty()
     */
    @Override
    public Optional<Task> add(TaskFormDto taskFormDto) {
        return add(fromDto(taskFormDto));
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
     * Обновить в репозитории объект Task данными из объекта по передаваемому значению id
     *
     * @param id Значение поля id для обновляемого объекта Task
     * @param taskFormDto Объект TaskFormDto, из которого берутся данные для обновления
     * @return true в случае успешного обновления. Иначе -- false
     */
    @Override
    public boolean update(int id, TaskFormDto taskFormDto) {
        Optional<Task> taskOptional = findById(id);
        if (taskOptional.isEmpty()) {
            return false;
        }
        Task taskInDb = taskOptional.get();
        Task newTask = fromDto(taskFormDto);
        taskInDb.setDescription(newTask.getDescription());
        taskInDb.setPriority(newTask.getPriority());
        taskInDb.setCategories(newTask.getCategories());
        return repository.update(taskInDb);
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

    /**
     * Создать новый объект Task из передаваемого объекта TaskFormDto
     *
     * @param taskFormDto Объект TaskFormDto, из которого создается объект Task
     * @return Созданный объект Task
     */
    @Override
    public Task fromDto(TaskFormDto taskFormDto) {
        Task task = new Task();
        task.setId(taskFormDto.getId());
        task.setDescription(taskFormDto.getDescription());
        Optional<Priority> priority = priorityService.findById(taskFormDto.getPriorityId());
        task.setPriority(priority.orElse(null));
        Set<Category> categories = Arrays.stream(taskFormDto.getCategoryIds())
                .collect(
                        HashSet::new,
                        (categoriesResult, categoryId) -> {
                            Optional<Category> category = categoryService.findById(categoryId);
                            category.ifPresent(categoriesResult::add);
                        },
                        AbstractCollection::addAll
                );
        task.setCategories(categories);
        return task;
    }

    /**
     * Создать новый объект TaskFormDto из передаваемого объекта Task
     *
     * @param task Объект Task, из которого создается объект TaskFormDto
     * @return Созданный объект TaskFormDto
     */
    @Override
    public TaskFormDto toDto(Task task) {
        TaskFormDto taskFormDto = new TaskFormDto();
        taskFormDto.setId(task.getId());
        taskFormDto.setDescription(task.getDescription());
        Optional<Priority> priority = Optional.ofNullable(task.getPriority());
        priority.ifPresent(value -> taskFormDto.setPriorityId(value.getId()));
        taskFormDto.setCategoryIds(task.getCategories().stream()
                .mapToInt(Category::getId)
                .toArray()
        );
        return taskFormDto;
    }
}