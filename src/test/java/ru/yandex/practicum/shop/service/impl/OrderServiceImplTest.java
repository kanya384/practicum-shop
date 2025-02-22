package ru.yandex.practicum.shop.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.shop.mapper.OrderMapperImpl;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Order;
import ru.yandex.practicum.shop.model.OrderItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.OrderItemRepository;
import ru.yandex.practicum.shop.repository.OrderRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.OrderService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {OrderServiceImpl.class, OrderMapperImpl.class})
public class OrderServiceImplTest {
    @MockitoBean
    private OrderRepository orderRepository;

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
        var product = new Product(1L, "title", "description", "image", 1);
        when(cartService.getCartItems())
                .thenReturn(List.of(new CartItem(product, 1)));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(new Order(1L, List.of(new OrderItem(1L, product, 1)), LocalDate.now()));

        var order = orderService.placeOrder();

        assertNotNull(order);
        assertEquals(1, order.getItems().size());

        verify(orderItemRepository, times(1))
                .saveAll(any());
    }
}
