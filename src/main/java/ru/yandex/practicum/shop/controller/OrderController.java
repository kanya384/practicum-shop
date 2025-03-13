package ru.yandex.practicum.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.shop.service.OrderService;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /*@PostMapping
    public String placeOrder(Model model) {
        var orderResponse = orderService.placeOrder();
        return "redirect:orders/" + orderResponse.getId();
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "orders";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        return "order";
    }*/
}
