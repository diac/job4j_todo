package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.List;

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
}