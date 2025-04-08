package ru.yandex.practicum.shop.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {OrderMapperImpl.class, ProductMapperImpl.class})
public class OrderMapperTest {
    @Autowired
    private OrderMapper orderMapper;

    /*@Test
    void map_shouldMapOrderToOrderResponseDTO() {
        var order = new Order(1L, List.of(new OrderItem(1L, new Product(1L, "title", "description", "image", 100), null, 2)), LocalDate.now());

        OrderResponseDTO orderDto = orderMapper.map(order);

        assertNotNull(orderDto);
        assertNotNull(orderDto.getId());
        assertNotNull(orderDto.getCreatedAt());
    }*/

    @Test
    void map_shouldMapCartItemToOrderItem() {
        /*var cartItem = new CartItem(new Product(1L, "title", "description", "image", 100), 1);
        var order = new Order(1L, LocalDate.now());

        OrderItem orderItem = OrderMapper.map(cartItem);

        assertNotNull(orderItem);
        assertEquals(1L, orderItem.getProductId());
        assertEquals(1, orderItem.getCount());*/
    }

}
