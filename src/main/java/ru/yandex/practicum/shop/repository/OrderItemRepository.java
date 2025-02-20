package ru.yandex.practicum.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.shop.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
