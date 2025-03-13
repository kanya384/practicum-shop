package ru.yandex.practicum.shop.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.shop.model.OrderItem;

@Repository
public interface OrderItemRepository extends R2dbcRepository<OrderItem, Long> {
}
