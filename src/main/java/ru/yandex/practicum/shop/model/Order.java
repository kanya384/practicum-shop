package ru.yandex.practicum.shop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "order_id", referencedColumnName = "id")
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
