package ru.yandex.practicum.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.model.User;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.PaymentsService;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final PaymentsService paymentsService;

    @GetMapping
    public Mono<String> getCartItems(Model model, Authentication authentication) {
        model.addAttribute("items", cartService.returnCartItems());

        model.addAttribute("sum", cartService.cartSum());

        var userId = ((User) authentication.getPrincipal()).getId();
        model.addAttribute("balance", paymentsService.getBalance(userId));

        return Mono.just("cart");
    }

    @PostMapping("/put-money")
    public Mono<String> putMoneyToBalance(Model model, Authentication authentication) {
        var userId = ((User) authentication.getPrincipal()).getId();

        return paymentsService.put5kToBalance(userId)
                .flatMap(p -> Mono.just("redirect:/cart"));
    }

    @PostMapping("/add/{productId}")
    public Mono<String> addProductToCart(@PathVariable("productId") Long productId) {
        return cartService.addItemToCart(productId)
                .map(p -> "redirect:/cart");
    }

    @PostMapping("/remove/{productId}")
    public Mono<String> removeProductFromCart(@PathVariable("productId") Long productId) {
        return cartService.removeItemFromCart(productId)
                .map(p -> "redirect:/products");
    }

    @PostMapping("/inc/{productId}")
    public Mono<String> increaseItemCount(@PathVariable("productId") Long productId) {
        return cartService.increaseItemCount(productId)
                .map(p -> "redirect:/cart");
    }

    @PostMapping("/dec/{productId}")
    public Mono<String> decreaseItemCount(@PathVariable("productId") Long productId) {
        return cartService.decreaseItemCount(productId)
                .map(p -> "redirect:/cart");
    }
}
