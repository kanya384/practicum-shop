package ru.yandex.practicum.shop.dto.order;

import lombok.*;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Long id;
    private ProductResponseDTO product;
    private Integer count;
    private Integer totalPrice;
}
