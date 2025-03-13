package ru.yandex.practicum.shop.model;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.Id;

@Entity
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
