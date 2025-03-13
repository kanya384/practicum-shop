package ru.yandex.practicum.shop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.shop.AbstractTestContainer;
import ru.yandex.practicum.shop.model.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ProductRepositoryTest extends AbstractTestContainer {
    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll()
                .then(productRepository.save(new Product(null, "title_test_1", "description", "image1", 100)))
                .then(productRepository.save(new Product(null, "title_2", "description_test", "image2", 200)))
                .block();
    }

    @Test
    void findAllByTitleOrDescription_shouldReturnAllProductsFilteredByTitleOrDescription() {
        productRepository
                .findAllByTitleOrDescription("test", PageRequest.of(0, 100))
                .count()
                .doOnNext(count -> assertEquals(2, count))
                .block();
    }

    @Test
    void countAllByTitleOrDescription_shouldReturnCountOfProductsFilteredByTitleOrDescription() {
        productRepository
                .countAllByTitleOrDescription("test")
                .doOnNext(count -> assertEquals(2, count))
                .block();
    }

    @Test
    void countAll_shouldReturnCountOfProducts() {
        productRepository.countAll()
                .doOnNext(res -> assertEquals(2, res))
                .block();
    }
}
