package ru.yandex.practicum.shop.dto.cart;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;

@Getter
@Setter
public class CartItemResponseDTO {
    private ProductResponseDTO product;
    private Integer count;
}
