package ru.yandex.practicum.shop.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.yandex.practicum.shop.model.Order;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, Long> {
    @Query("SELECT o FROM Order o join fetch o.items as i join fetch i.product")
    Flux<Order> findAlOrdersWithOrderItems();
}
