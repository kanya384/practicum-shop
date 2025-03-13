package ru.yandex.practicum.shop.model;


import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private Long id;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;

    @CreatedDate
    private LocalDate createdAt;

    public void addItem(OrderItem orderItem) {
        if (items == null) {
            items = new ArrayList<>();
        }

        items.add(orderItem);
    }
}
