package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.List;
import java.util.Optional;

@Controller
@ThreadSafe
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping()
    public String index(Model model) {
        List<Task> tasks = taskService.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks/index";
    }

    @GetMapping("/completed")
    public String completedIndex(Model model) {
        List<Task> tasks = taskService.findAllByDone(true);
        model.addAttribute("tasks", tasks);
        return "tasks/index";
    }

    @GetMapping("/incomplete")
    public String incompleteIndex(Model model) {
        List<Task> tasks = taskService.findAllByDone(false);
        model.addAttribute("tasks", tasks);
        return "tasks/index";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping()
    public String store(@ModelAttribute Task task) {
        taskService.add(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Задача не найдена");
            return "redirect:/tasks";
        }
        model.addAttribute("task", task.get());
        return "tasks/view";
    }

    @PatchMapping("/{id}/complete")
    public String complete(@PathVariable("id") int id,  RedirectAttributes redirectAttributes) {
        if (taskService.completeById(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Задача отмечена как завершенная");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось обновить статус задачи");
        }
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,  Model model, RedirectAttributes redirectAttributes) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Задача не найдена");
            return "redirect:/tasks";
        }
        model.addAttribute("task", task.get());
        return "tasks/edit";
    }

    @PatchMapping("/{id}")
    public String patch(
            @ModelAttribute("task") Task task,
            @PathVariable("id") int id,
            RedirectAttributes redirectAttributes
    ) {
        if (taskService.updateDescriptionById(id, task.getDescription())) {
            redirectAttributes.addFlashAttribute("successMessage", "Задача обновлена");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось обновить задачу");
        }
        return "redirect:/tasks";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        if (taskService.deleteById(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Задача удалена");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось удалить задачу");
        }
        return "redirect:/tasks";
    }
}