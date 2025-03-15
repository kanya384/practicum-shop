package ru.yandex.practicum.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.service.OrderService;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Mono<String> placeOrder(Model model) {
        return orderService.placeOrder()
                .map(m -> model.addAttribute("order", m))
                .flatMap(o -> Mono.just("order"));
    }

    @GetMapping
    public Mono<String> findAll(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return Mono.just("orders");
    }

    @GetMapping("/{id}")
    public Mono<String> findById(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        return Mono.just("order");
    }
}
