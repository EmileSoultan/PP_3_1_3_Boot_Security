package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userServiceImpl) {
        this.userService = userServiceImpl;
    }

    @GetMapping()
    public String showAllUsers(Model model) {
        List<User> users = userService.listAll();
        model.addAttribute("users", users);
        return "/users";
    }

    @GetMapping("/new")
    public String createNewUser(Model model, User user) {
        List<Role> roles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "user_form";
    }

    @PostMapping("/save")
    public String saveUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = userService.listRoles();
            model.addAttribute("roles", roles);
            return "user_form";
        }
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.findById(id));
        List<Role> roles = userService.listRoles();
        model.addAttribute("roles", roles);
        return "edit_user";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @PathVariable("id") long id, Model model) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = userService.listRoles();
            model.addAttribute("roles", roles);
            return "edit_user";
        }
        userService.edit(id, user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}

