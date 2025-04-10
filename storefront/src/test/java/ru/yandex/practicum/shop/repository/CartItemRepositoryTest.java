package ru.yandex.practicum.shop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;
import ru.yandex.practicum.shop.AbstractTestContainer;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DirtiesContext
public class CartItemRepositoryTest extends AbstractTestContainer {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UsersRepository userRepository;

    @BeforeEach
    void setUp() {

        productRepository.deleteAll()
                .block();

        cartItemRepository.deleteAll()
                .block();

        userRepository.deleteAll()
                .block();
    }

    @Test
    void findByUserIdAndByProductId_shouldReturnCartItemByUserAndProductId() {
        var product = new Product(null, "title_test_1", "description", "image1", 100);

        productRepository.save(product)
                .block();

        var user = new User(null, "login", "password", "role", null, null);
        userRepository.save(user)
                .block();

        var cartItem = new CartItem(user.getId(), product.getId(), 1);

        cartItemRepository.save(cartItem)
                .block();

        StepVerifier.create(cartItemRepository
                        .findByUserIdAndProductId(user.getId(), product.getId()))
                .assertNext(ci -> assertEquals(cartItem.getId(), ci.getId()))
                .verifyComplete();
    }

    @Test
    void findByUserId_shouldReturnCartItemsListOfUser() {
        var product = new Product(null, "title_test_1", "description", "image1", 100);

        productRepository.save(product)
                .block();

        var user = new User(null, "login", "password", "role", null, null);
        userRepository.save(user)
                .block();

        var cartItem = new CartItem(user.getId(), product.getId(), 1);

        cartItemRepository.save(cartItem)
                .block();

        StepVerifier.create(cartItemRepository
                        .findByUserId(user.getId())
                        .collectList())
                .assertNext(items -> assertEquals(1, items.size()))
                .verifyComplete();
    }

    @Test
    void countByUserId_shouldReturnCartItemsCountOfUser() {
        var product = new Product(null, "title_test_1", "description", "image1", 100);

        productRepository.save(product)
                .block();

        var product2 = new Product(null, "title_test_1", "description", "image1", 100);

        productRepository.save(product2)
                .block();

        var user = new User(null, "login", "password", "role", null, null);
        userRepository.save(user)
                .block();

        var cartItem = new CartItem(user.getId(), product.getId(), 1);

        cartItemRepository.save(cartItem)
                .block();

        cartItemRepository.save(new CartItem(user.getId(), product2.getId(), 1))
                .block();

        StepVerifier.create(cartItemRepository
                        .countByUserId(user.getId()))
                .assertNext(count -> assertEquals(2, count))
                .verifyComplete();
    }

    @Test
    void deleteByUserId_shouldClearCartItemsOfUser() {
        var product1 = new Product(null, "title_test_1", "description", "image1", 100);
        var product2 = new Product(null, "title_test_1", "description", "image1", 100);

        productRepository.save(product1)
                .then(productRepository.save(product2))
                .block();
        var user = new User(null, "login", "password", "role", null, null);
        userRepository.save(user)
                .block();

        var cartItem1 = new CartItem(user.getId(), product1.getId(), 1);

        var cartItem2 = new CartItem(user.getId(), product2.getId(), 1);

        cartItemRepository.save(cartItem1)
                .then(cartItemRepository.save(cartItem2))
                .block();

        StepVerifier.create(cartItemRepository
                        .deleteByUserId(user.getId())
                        .then(cartItemRepository.countByUserId(user.getId())))
                .assertNext(count -> assertEquals(0, count))
                .verifyComplete();
    }
}
