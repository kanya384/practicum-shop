package ru.yandex.practicum.shop.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    private Product product;

    private Integer count;

    public Integer getTotalPrice() {
        return product.getPrice() * count;
    }

    public void inc() {
        count++;
    }

    public void dec() {
        count--;
    }
}
