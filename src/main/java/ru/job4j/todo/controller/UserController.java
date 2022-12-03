package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TimeZone;

@Controller
@ThreadSafe
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/register")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        var timeZones = new ArrayList<TimeZone>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            timeZones.add(TimeZone.getTimeZone(timeId));
        }
        model.addAttribute("timeZones", timeZones);
        return "users/register";
    }

    @PostMapping("/users/register")
    public String register(@ModelAttribute User user) {
        userService.register(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        Optional<User> userInDb = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (userInDb.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка авторизации");
            return "redirect:/login";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userInDb.get());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}