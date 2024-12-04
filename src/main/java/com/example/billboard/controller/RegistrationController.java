package com.example.billboard.controller;

import com.example.billboard.dto.UserDto;
import com.example.billboard.model.User;
import com.example.billboard.model.enums.UserRole;
import com.example.billboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    @GetMapping("/login")
    public String login(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto user) {
        return userService.registerOrLoginUser(user);
    }

}
