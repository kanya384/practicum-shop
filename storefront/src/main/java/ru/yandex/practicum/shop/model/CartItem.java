package ru.yandex.practicum.shop.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cart_item")
public class CartItem {
    @Id
    private Long id;

    private Long userId;

    private Long productId;

    private Integer count;

    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;

    public CartItem(Long userId, Long productId, Integer count) {
        this.userId = userId;
        this.productId = productId;
        this.count = count;
    }

    public void inc() {
        count++;
    }

    public void dec() {
        count--;
    }
}
