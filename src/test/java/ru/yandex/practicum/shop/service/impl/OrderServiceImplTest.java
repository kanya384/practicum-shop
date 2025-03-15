package ru.yandex.practicum.shop.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.shop.exception.NoProductsInOrderException;
import ru.yandex.practicum.shop.mapper.OrderMapperImpl;
import ru.yandex.practicum.shop.mapper.ProductMapperImpl;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Order;
import ru.yandex.practicum.shop.model.OrderItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.OrderItemRepository;
import ru.yandex.practicum.shop.repository.OrderRepository;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.OrderService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {OrderServiceImpl.class, ProductMapperImpl.class, OrderMapperImpl.class})
public class OrderServiceImplTest {
    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private OrderItemRepository orderItemRepository;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        reset(cartService);
        reset(orderRepository);
        reset(orderItemRepository);
    }

    @Test
    void placeOrder_shouldPlaceOrder() {
        Product product = new Product(1L, "title", "description", "image", 1);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(Mono.just(new Order(1L, LocalDate.now())));

        when(cartService.getCartItems())
                .thenReturn(List.of(new CartItem(product, 1)));

        when(orderItemRepository.saveAll(anyList()))
                .thenReturn(Flux.just(new OrderItem(1L, 1L, 1L, 1)));

        Order order = new Order(1L, LocalDate.now());
        OrderItem orderItem = new OrderItem(1L, 1L, 1L, 1);

        when(cartService.clearCart())
                .thenReturn(Mono.empty());
        when(orderRepository.findById(1L))
                .thenReturn(Mono.just(order));
        when(orderItemRepository.findAllByOrderId(1L))
                .thenReturn(Flux.just(orderItem));
        when(productRepository.findAllById(List.of(1L)))
                .thenReturn(Flux.just(product));


        StepVerifier.create(orderService.placeOrder())
                .expectNextMatches(o -> o.getId() == 1L)
                .verifyComplete();

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    void placeOrder_shouldNotPlaceOrderIfCartIsEmpty() {
        when(orderRepository.save(any(Order.class)))
                .thenReturn(Mono.just(new Order(1L, LocalDate.now())));

        when(cartService.getCartItems())
                .thenReturn(List.of());

        StepVerifier.create(orderService.placeOrder())
                .expectError(NoProductsInOrderException.class)
                .verify();
    }

    @Test
    void findById_shouldFindOrderById() {
        Order order = new Order(1L, LocalDate.now());
        OrderItem orderItem = new OrderItem(1L, 1L, 1L, 1);
        Product product = new Product(1L, "title", "description", "image", 1);
        when(orderRepository.findById(1L))
                .thenReturn(Mono.just(order));
        when(orderItemRepository.findAllByOrderId(1L))
                .thenReturn(Flux.just(orderItem));
        when(productRepository.findAllById(List.of(1L)))
                .thenReturn(Flux.just(product));

        StepVerifier.create(orderService.findById(1L))
                .expectNextMatches(o -> o.getItems().getFirst().getProduct().getId() == 1L)
                .verifyComplete();
    }
}
