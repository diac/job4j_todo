package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Controller
@ThreadSafe
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    @GetMapping("")
    public String index(Model model, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");
        ZoneId zoneId = ZoneId.of(user.getUserZone());
        List<Task> tasks = taskService.findAll(zoneId);
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
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String store(
            @ModelAttribute("task") Task task,
            @RequestParam("priorityId") int priorityId,
            @RequestParam("categoryIds") int[] categoryIds,
            HttpServletRequest request
    ) {
        HttpSession httpSession = request.getSession();
        task.setUser((User) httpSession.getAttribute("user"));
        task.setPriority(priorityService.findById(priorityId).orElse(null));
        task.setCategories(categoryService.findAllByIds(categoryIds));
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
    public String complete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        if (taskService.completeById(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Задача отмечена как завершенная");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось обновить статус задачи");
        }
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Задача не найдена");
            return "redirect:/tasks";
        }
        model.addAttribute("task", task.get());
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/edit";
    }

    @PatchMapping("/{id}")
    public String patch(
            @ModelAttribute("task") Task task,
            @PathVariable("id") int id,
            @RequestParam("priorityId") int priorityId,
            @RequestParam("categoryIds") int[] categoryIds,
            RedirectAttributes redirectAttributes
    ) {
        Task taskInDb = taskService.findById(id).orElse(new Task());
        taskInDb.setDescription(task.getDescription());
        taskInDb.setPriority(priorityService.findById(priorityId).orElse(null));
        taskInDb.setCategories(categoryService.findAllByIds(categoryIds));
        if (taskService.update(taskInDb)) {
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