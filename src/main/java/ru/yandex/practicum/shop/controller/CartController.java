package ru.yandex.practicum.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.service.CartService;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public Mono<String> getCartItems(Model model) {
        model.addAttribute("items", cartService.getCartItems());
        model.addAttribute("sum", cartService.getCartItems()
                .stream().map(ci -> ci.getCount() * ci.getProduct().getPrice())
                .reduce(0, Integer::sum));
        return Mono.just("cart");
    }

    @PostMapping("/add/{productId}")
    public Mono<String> addProductToCart(@RequestHeader(value = HttpHeaders.REFERER) final String referrer,
                                         @PathVariable("productId") Long productId) {
        return cartService.addItemToCart(productId)
                .map(p -> "cart");
    }

    @PostMapping("/remove/{productId}")
    public Mono<String> removeProductFromCart(@RequestHeader(value = HttpHeaders.REFERER) final String referrer,
                                              @PathVariable("productId") Long productId) {

        return cartService.removeItemFromCart(productId)
                .map(p -> "cart");
    }

    @PostMapping("/inc/{productId}")
    public Mono<String> increaseItemCount(@RequestHeader(value = HttpHeaders.REFERER) final String referrer,
                                          @PathVariable("productId") Long productId) {
        return cartService.increaseItemCount(productId)
                .map(p -> "cart");
    }

    @PostMapping("/dec/{productId}")
    public Mono<String> decreaseItemCount(@RequestHeader(value = HttpHeaders.REFERER) final String referrer,
                                          @PathVariable("productId") Long productId) {
        return cartService.decreaseItemCount(productId)
                .map(p -> "cart");
    }
}
