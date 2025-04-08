package ru.yandex.practicum.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.auth.dto.CreateUserDTO;
import ru.yandex.practicum.auth.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public Mono<String> registrationForm(Model model) {
        return Mono.just("registration");
    }

    @PostMapping("/register")
    public Mono<String> save(@ModelAttribute CreateUserDTO data) {
        return userService.save(data)
                .map(r -> "registration");
    }
}
