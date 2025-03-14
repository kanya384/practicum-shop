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
    public String getCartItems(Model model) {
        model.addAttribute("items", cartService.getCartItems());
        model.addAttribute("sum", cartService.getCartItems()
                .stream().map(ci -> ci.getCount() * ci.getProduct().getPrice())
                .reduce(0, Integer::sum));
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public Mono<String> addProductToCart(@RequestHeader(value = HttpHeaders.REFERER) final String referrer,
                                         @PathVariable("productId") Long productId) {
        return cartService.addItemToCart(productId)
                .flatMap(p -> Mono.just("cart"));
    }

    @PostMapping("/remove/{productId}")
    public String removeProductFromCart(@RequestHeader(value = HttpHeaders.REFERER) final String referrer,
                                        @PathVariable("productId") Long productId) {
        cartService.removeItemFromCart(productId);
        return "redirect:" + referrer;
    }

    @PostMapping("/inc/{productId}")
    public String increaseItemCount(@RequestHeader(value = HttpHeaders.REFERER) final String referrer,
                                    @PathVariable("productId") Long productId) {
        cartService.increaseItemCount(productId);
        return "redirect:" + referrer;
    }

    @PostMapping("/dec/{productId}")
    public String decreaseItemCount(@RequestHeader(value = HttpHeaders.REFERER) final String referrer,
                                    @PathVariable("productId") Long productId) {
        cartService.decreaseItemCount(productId);
        return "redirect:" + referrer;
    }
}
