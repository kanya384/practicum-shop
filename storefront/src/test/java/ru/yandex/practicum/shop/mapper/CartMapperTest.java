package ru.yandex.practicum.shop.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {CartMapperImpl.class, ProductMapperImpl.class})
public class CartMapperTest {
    @Autowired
    private CartMapper cartMapper;

    @Test
    void map_shouldMapCartItemToCartItemResponseDTO() {
        /*var cartItem = new CartItem(new Product(1L, "title", "description", "image", 100), 1);

        CartItemResponseDTO cartItemResponseDTO = cartMapper.map(cartItem);

        assertNotNull(cartItemResponseDTO);
        assertNotNull(cartItemResponseDTO.getProduct());
        assertNotNull(cartItemResponseDTO.getCount());*/
    }

}
