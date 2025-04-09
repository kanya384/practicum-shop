package ru.yandex.practicum.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class DefaultController {
    @GetMapping("/")
    public Mono<String> mainPage() {
        return Mono.just("redirect:/products");
    }
}
