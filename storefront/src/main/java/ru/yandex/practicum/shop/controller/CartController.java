package ru.yandex.practicum.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.service.CartService;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public Mono<String> getCartItems(Model model) {
        model.addAttribute("items", cartService.returnCartItems());
        model.addAttribute("sum", cartService.returnCartItems()
                .map(ci -> ci.getCount() * ci.getProduct().getPrice())
                .reduce(0, Integer::sum));
        return Mono.just("cart");
    }

    @PostMapping("/add/{productId}")
    public Mono<String> addProductToCart(@PathVariable("productId") Long productId) {
        return cartService.addItemToCart(productId)
                .map(p -> "cart");
    }

    @PostMapping("/remove/{productId}")
    public Mono<String> removeProductFromCart(@PathVariable("productId") Long productId) {

        return cartService.removeItemFromCart(productId)
                .map(p -> "cart");
    }

    @PostMapping("/inc/{productId}")
    public Mono<String> increaseItemCount(@PathVariable("productId") Long productId) {
        return cartService.increaseItemCount(productId)
                .map(p -> "cart");
    }

    @PostMapping("/dec/{productId}")
    public Mono<String> decreaseItemCount(@PathVariable("productId") Long productId) {
        return cartService.decreaseItemCount(productId)
                .map(p -> "cart");
    }
}
