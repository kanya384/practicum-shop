package ru.yandex.practicum.shop.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String image;
    private Integer price;
    private Integer count;
}
