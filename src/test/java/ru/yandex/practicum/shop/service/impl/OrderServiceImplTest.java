package ru.yandex.practicum.shop.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.shop.dto.order.OrderItemResponseDTO;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.exception.NoProductsInOrderException;
import ru.yandex.practicum.shop.mapper.OrderMapper;
import ru.yandex.practicum.shop.mapper.ProductMapperImpl;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Order;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.OrderItemRepository;
import ru.yandex.practicum.shop.repository.OrderRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.OrderService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {OrderServiceImpl.class, ProductMapperImpl.class})
public class OrderServiceImplTest {
    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private OrderItemRepository orderItemRepository;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private OrderMapper orderMapper;

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
        var product = new Product(1L, "title", "description", "image", 1);
        when(cartService.getCartItems())
                .thenReturn(Flux.just(new CartItem(product, 1)));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(Mono.just(new Order(1L, List.of(), LocalDate.now())));

        var orderResponse = OrderResponseDTO.builder()
                .id(1L)
                .items(List.of(OrderItemResponseDTO.builder()
                        .id(1L)
                        .product(ProductResponseDTO.builder()
                                .id(product.getId())
                                .title(product.getTitle())
                                .description(product.getDescription())
                                .build())
                        .build()))
                .createdAt(LocalDate.now())
                .build();

        when(orderMapper.map(any(Order.class)))
                .thenReturn(orderResponse);

        StepVerifier.create(orderService.placeOrder())
                .expectNext(orderResponse)
                .verifyComplete();

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    void placeOrder_shouldNotPlaceOrderIfCartIsEmpty() {
        when(cartService.getCartItems())
                .thenReturn(Flux.empty());

        StepVerifier.create(orderService.placeOrder())
                .expectError(NoProductsInOrderException.class)
                .verify();
    }
}
