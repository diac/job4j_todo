package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@ThreadSafe
public class DashboardController {

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/tasks";
    }
}