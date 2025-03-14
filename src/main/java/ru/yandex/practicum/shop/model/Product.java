package ru.yandex.practicum.shop.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    private Long id;

    private String title;

    private String description;

    private String image;

    private Integer price;
}
