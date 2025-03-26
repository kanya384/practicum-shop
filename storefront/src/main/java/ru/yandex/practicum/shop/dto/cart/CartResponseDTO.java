package ru.yandex.practicum.shop.dto.cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartResponseDTO {
    private List<CartItemResponseDTO> items;

    public Integer sum() {
        return items.stream()
                .mapToInt(cartItem -> cartItem.getProduct().getPrice() * cartItem.getCount())
                .sum();
    }
}
