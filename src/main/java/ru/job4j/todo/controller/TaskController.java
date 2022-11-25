package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.List;
import java.util.Optional;

@Controller
@ThreadSafe
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/")
    public String index(Model model) {
        List<Task> tasks = taskService.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks/index";
    }

    @GetMapping("/tasks/completed")
    public String completedIndex(Model model) {
        List<Task> tasks = taskService.findAllByDone(true);
        model.addAttribute("tasks", tasks);
        return "tasks/index";
    }

    @GetMapping("/tasks/incomplete")
    public String incompleteIndex(Model model) {
        List<Task> tasks = taskService.findAllByDone(false);
        model.addAttribute("tasks", tasks);
        return "tasks/index";
    }

    @GetMapping("/tasks/new")
    public String create(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping("/tasks")
    public String store(@ModelAttribute Task task) {
        taskService.add(task);
        return "redirect:/";
    }

    @GetMapping("/tasks/{id}")
    public String view(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Задача не найдена");
            return "redirect:/";
        }
        model.addAttribute("task", task.get());
        return "tasks/view";
    }

    @PostMapping("/tasks/{id}/complete")
    public String complete(@PathVariable("id") int id,  RedirectAttributes redirectAttributes) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Задача не найдена");
        } else if (taskService.complete(task.get())) {
            redirectAttributes.addFlashAttribute("successMessage", "Задача отмечена как завершенная");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось обновить статус задачи");
        }
        return "redirect:/";
    }
}