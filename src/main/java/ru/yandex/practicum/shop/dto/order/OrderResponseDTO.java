package ru.yandex.practicum.shop.dto.order;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
