package ru.yandex.practicum.shop.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.shop.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE lower(p.title) like lower(concat('%', :search, '%')) or lower(p.description) like lower(concat('%', :search, '%'))")
    List<Product> findAllByTitleOrDescription(String search, PageRequest pageRequest);

    @Query("SELECT count(*) FROM Product p WHERE lower(p.title) like lower(concat('%', :search, '%')) or lower(p.description) like lower(concat('%', :search, '%'))")
    Integer countAllByTitleOrDescription(String search);

    @Query("SELECT count(*) FROM Product")
    Integer countAll();
}
