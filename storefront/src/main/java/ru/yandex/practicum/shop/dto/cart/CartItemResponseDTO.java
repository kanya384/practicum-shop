package ru.yandex.practicum.shop.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;

@Getter
@Setter
@AllArgsConstructor
public class CartItemResponseDTO {
    private ProductResponseDTO product;
    private Integer count;
}
