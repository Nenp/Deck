package tech.sanak.deck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.sanak.deck.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("username", "");
        model.addAttribute("password", "");
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String username, @RequestParam String password, Model model) {
        if (userService.existsByUsername(username)) {
            model.addAttribute("error", "Username already exists.");
            return "register";
        }

        userService.register(username, password);
        return "redirect:/login";
    }
}
