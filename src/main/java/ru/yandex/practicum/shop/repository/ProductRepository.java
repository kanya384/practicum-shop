package ru.yandex.practicum.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.shop.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
