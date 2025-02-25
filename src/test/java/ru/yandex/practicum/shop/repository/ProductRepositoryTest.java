package ru.yandex.practicum.shop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.shop.model.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        productRepository.save(new Product(null, "title_test_1", "description", "image1", 100));
        productRepository.save(new Product(null, "title_2", "description_test", "image2", 200));
    }

    @Test
    void findAllByTitleOrDescription_shouldReturnAllProductsFilteredByTitleOrDescription() {
        List<Product> products = productRepository
                .findAllByTitleOrDescription("test", PageRequest.of(0, 100));
        assertEquals(2, products.size());
    }

    @Test
    void countAllByTitleOrDescription_shouldReturnCountOfProductsFilteredByTitleOrDescription() {
        Integer count = productRepository
                .countAllByTitleOrDescription("test");
        assertEquals(2, count);
    }

    @Test
    void countAll_shouldReturnCountOfProducts() {
        Integer count = productRepository
                .countAll();
        assertEquals(2, count);
    }
}
