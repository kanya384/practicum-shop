package ru.yandex.practicum.shop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;
import ru.yandex.practicum.shop.AbstractTestContainer;
import ru.yandex.practicum.shop.model.Product;

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
        StepVerifier.create(productRepository
                        .findAllByTitleOrDescription("test", PageRequest.of(0, 100)))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void countAllByTitleOrDescription_shouldReturnCountOfProductsFilteredByTitleOrDescription() {
        StepVerifier.create(productRepository
                        .countAllByTitleOrDescription("test"))
                .expectNext(2)
                .verifyComplete();
    }

    @Test
    void countAll_shouldReturnCountOfProducts() {
        StepVerifier.create(productRepository.countAll())
                .expectNext(2)
                .verifyComplete();
    }
}
