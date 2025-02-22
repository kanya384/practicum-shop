package ru.yandex.practicum.shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
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
        items.add(orderItem);
    }
}
