package ru.yandex.practicum.shop.dto.product;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String image;
    private Integer price;
}
