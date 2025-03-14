package ru.yandex.practicum.shop.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.model.Product;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long> {
    Flux<Product> findAllBy(Pageable pageable);

    @Query("SELECT * FROM Product p WHERE lower(p.title) like lower(concat('%', :search, '%')) or lower(p.description) like lower(concat('%', :search, '%'))")
    Flux<Product> findAllByTitleOrDescription(String search, Pageable pageable);

    @Query("SELECT count(*) FROM Product p WHERE lower(p.title) like lower(concat('%', :search, '%')) or lower(p.description) like lower(concat('%', :search, '%'))")
    Mono<Integer> countAllByTitleOrDescription(String search);

    @Query("SELECT count(*) FROM Product")
    Mono<Integer> countAll();
}
