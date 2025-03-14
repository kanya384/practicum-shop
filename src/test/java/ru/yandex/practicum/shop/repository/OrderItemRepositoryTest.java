package ru.yandex.practicum.shop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;
import ru.yandex.practicum.shop.AbstractTestContainer;
import ru.yandex.practicum.shop.model.Order;
import ru.yandex.practicum.shop.model.OrderItem;
import ru.yandex.practicum.shop.model.Product;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class OrderItemRepositoryTest extends AbstractTestContainer {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    void save_shouldSaveOrderItem() {
        Product product = productRepository.save(new Product(null, "title_test_1", "description", "image1", 100)).block();
        Order order = orderRepository.save(new Order()).block();
        StepVerifier.create(
                        orderRepository.save(new Order())
                                .map(o -> List.of(new OrderItem(null, product.getId(), order.getId(), 1)))
                                .map(orderItemRepository::saveAll))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findAllByOrderId_shouldReturnAllOrderItems() {
        Product product = productRepository.save(new Product(null, "title_test_1", "description", "image1", 100)).block();
        Order order = orderRepository.save(new Order()).block();
        orderItemRepository.saveAll(List.of(new OrderItem(null, product.getId(), order.getId(), 1), new OrderItem(null, product.getId(), order.getId(), 1)))
                .blockLast();
        StepVerifier.create(
                        orderItemRepository.findAllByOrderId(1L))
                .expectNextCount(2)
                .verifyComplete();
    }
}
