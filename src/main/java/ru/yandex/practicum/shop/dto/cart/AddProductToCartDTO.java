package ru.yandex.practicum.shop.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductToCartDTO {
    @NotNull
    private Long productId;
}
