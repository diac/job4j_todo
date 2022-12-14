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
import ru.job4j.todo.util.DateFormat;

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
        List<Task> tasks = taskService.findAll(getCurrentUserZoneId(request));
        model.addAttribute("tasks", tasks);
        model.addAttribute("dateFormat", DateFormat.defaultFormatter());
        return "tasks/index";
    }

    @GetMapping("/completed")
    public String completedIndex(Model model, HttpServletRequest request) {
        List<Task> tasks = taskService.findAllByDone(true, getCurrentUserZoneId(request));
        model.addAttribute("tasks", tasks);
        model.addAttribute("dateFormat", DateFormat.defaultFormatter());
        return "tasks/index";
    }

    @GetMapping("/incomplete")
    public String incompleteIndex(Model model, HttpServletRequest request) {
        List<Task> tasks = taskService.findAllByDone(false, getCurrentUserZoneId(request));
        model.addAttribute("tasks", tasks);
        model.addAttribute("dateFormat", DateFormat.defaultFormatter());
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
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        HttpSession httpSession = request.getSession();
        try {
            taskService.add(task, priorityId, categoryIds, (User) httpSession.getAttribute("user"));
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    String.format("???? ?????????????? ?????????????? ?????????? ????????????. ??????????????: %s", e.getMessage())
            );
            return "redirect:/tasks/new";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String view(
            @PathVariable("id") int id,
            Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Task> task = taskService.findById(id, getCurrentUserZoneId(request));
        if (task.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "???????????? ???? ??????????????");
            return "redirect:/tasks";
        }
        model.addAttribute("task", task.get());
        model.addAttribute("dateFormat", DateFormat.defaultFormatter());
        return "tasks/view";
    }

    @PatchMapping("/{id}/complete")
    public String complete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        if (taskService.completeById(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "???????????? ???????????????? ?????? ??????????????????????");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "???? ?????????????? ???????????????? ???????????? ????????????");
        }
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "???????????? ???? ??????????????");
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
        try {
            if (taskService.update(id, task.getDescription(), priorityId, categoryIds)) {
                redirectAttributes.addFlashAttribute("successMessage", "???????????? ??????????????????");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "???? ?????????????? ???????????????? ????????????");
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    String.format("???? ?????????????? ???????????????? ????????????. ??????????????: %s", e.getMessage())
            );
            return String.format("redirect:/tasks/%d/edit", id);
        }
        return "redirect:/tasks";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        if (taskService.deleteById(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "???????????? ??????????????");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "???? ?????????????? ?????????????? ????????????");
        }
        return "redirect:/tasks";
    }

    private ZoneId getCurrentUserZoneId(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");
        return ZoneId.of(user.getUserZone());
    }
}