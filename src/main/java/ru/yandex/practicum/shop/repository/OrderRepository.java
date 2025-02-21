package ru.yandex.practicum.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.shop.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
