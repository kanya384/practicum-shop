package ru.yandex.practicum.shop.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    public Order(Long userId) {
        this.userId = userId;
    }

    @Id
    private Long id;

    private Long userId;

    @CreatedDate
    private LocalDate createdAt;
}
