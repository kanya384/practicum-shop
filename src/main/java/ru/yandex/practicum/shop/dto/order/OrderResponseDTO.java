package ru.yandex.practicum.shop.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private List<OrderItemResponseDTO> items;
    private LocalDate createdAt;

    public Integer sum() {
        return items.stream()
                .mapToInt(OrderItemResponseDTO::totalPrice)
                .sum();
    }
}
