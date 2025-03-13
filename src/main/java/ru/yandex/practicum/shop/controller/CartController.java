package ru.yandex.practicum.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.shop.service.CartService;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /*@GetMapping
    public String getCartItems(Model model) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setItems(cartService.returnCartItems());
        model.addAttribute("cart", cartResponseDTO);
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addProductToCart(@RequestHeader(value = HttpHeaders.REFERER) final String referrer,
                                   @PathVariable("productId") Long productId) {
        cartService.addItemToCart(productId);
        return "redirect:" + referrer;
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
    }*/
}
