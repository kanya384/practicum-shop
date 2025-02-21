package ru.yandex.practicum.shop.dto.order;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;

@Getter
@Setter
public class OrderItemResponseDTO {
    private Long id;
    private ProductResponseDTO productResponseDTO;
    private Integer count;
    private Integer totalPrice;
}
