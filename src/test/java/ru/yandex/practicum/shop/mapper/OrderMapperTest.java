package ru.yandex.practicum.shop.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Order;
import ru.yandex.practicum.shop.model.OrderItem;
import ru.yandex.practicum.shop.model.Product;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {OrderMapperImpl.class, ProductMapperImpl.class})
public class OrderMapperTest {
    @Autowired
    private OrderMapper orderMapper;

    @Test
    void map_shouldMapOrderToOrderResponseDTO() {
        var order = new Order(1L, List.of(new OrderItem(1L, new Product(1L, "title", "description", "image", 100), null, 2)), LocalDate.now());

        OrderResponseDTO orderDto = orderMapper.map(order);

        assertNotNull(orderDto);
        assertNotNull(orderDto.getId());
        assertNotNull(orderDto.getCreatedAt());
    }

    @Test
    void map_shouldMapCartItemToOrderItem() {
        var cartItem = new CartItem(new Product(1L, "title", "description", "image", 100), 1);
        var order = new Order(1L, List.of(), LocalDate.now());

        OrderItem orderItem = OrderMapper.map(cartItem);

        assertNotNull(orderItem);
        assertEquals(1L, orderItem.getProduct().getId());
        assertEquals(1, orderItem.getCount());
    }

}
