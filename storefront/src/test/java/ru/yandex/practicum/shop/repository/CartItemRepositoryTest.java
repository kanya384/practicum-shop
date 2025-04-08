package ru.yandex.practicum.shop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;
import ru.yandex.practicum.shop.AbstractTestContainer;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class CartItemRepositoryTest extends AbstractTestContainer {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        var product1 = new Product(null, "title_test_1", "description", "image1", 100);
        var product2 = new Product(null, "title_2", "description_test", "image2", 200);

        productRepository.deleteAll()
                .then(productRepository.save(product1))
                .then(productRepository.save(product2))
                .then(cartItemRepository.deleteAll())
                .block();
    }

    @Test
    void findByUserIdAndByProductId_shouldReturnCartItemByUserAndProductId() {
        var product = new Product(null, "title_test_1", "description", "image1", 100);

        productRepository.save(product)
                .block();

        var cartItem = new CartItem(userId, product.getId(), 1);

        cartItemRepository.save(cartItem)
                .block();

        StepVerifier.create(cartItemRepository
                        .findByUserIdAndProductId(userId, product.getId()))
                .assertNext(ci -> assertEquals(cartItem.getId(), ci.getId()))
                .verifyComplete();
    }

    @Test
    void findByUserId_shouldReturnCartItemsListOfUser() {
        var product = new Product(null, "title_test_1", "description", "image1", 100);

        productRepository.save(product)
                .block();

        var cartItem = new CartItem(userId, product.getId(), 1);

        cartItemRepository.save(cartItem)
                .block();

        cartItemRepository.save(new CartItem(1L, product.getId(), 1))
                .block();

        StepVerifier.create(cartItemRepository
                        .findByUserId(userId)
                        .collectList())
                .assertNext(items -> assertEquals(1, items.size()))
                .verifyComplete();
    }

    @Test
    void countByUserId_shouldReturnCartItemsCountOfUser() {
        var product = new Product(null, "title_test_1", "description", "image1", 100);

        productRepository.save(product)
                .block();

        var cartItem = new CartItem(userId, product.getId(), 1);

        cartItemRepository.save(cartItem)
                .block();

        cartItemRepository.save(new CartItem(1L, product.getId(), 1))
                .block();

        StepVerifier.create(cartItemRepository
                        .countByUserId(userId))
                .assertNext(count -> assertEquals(1, count))
                .verifyComplete();
    }

    @Test
    void deleteByUserId_shouldClearCartItemsOfUser() {
        var product1 = new Product(null, "title_test_1", "description", "image1", 100);
        var product2 = new Product(null, "title_test_1", "description", "image1", 100);

        productRepository.save(product1)
                .then(productRepository.save(product2))
                .block();

        var cartItem1 = new CartItem(userId, product1.getId(), 1);

        var cartItem2 = new CartItem(userId, product2.getId(), 1);

        cartItemRepository.save(cartItem1)
                .then(cartItemRepository.save(cartItem2))
                .block();

        StepVerifier.create(cartItemRepository
                        .deleteByUserId(userId)
                        .then(cartItemRepository.countByUserId(userId)))
                .assertNext(count -> assertEquals(0, count))
                .verifyComplete();
    }
}
