package ru.yandex.practicum.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.shop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
