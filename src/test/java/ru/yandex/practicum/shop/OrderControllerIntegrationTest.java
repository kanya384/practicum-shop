package ru.yandex.practicum.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.repository.OrderRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.OrderService;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrderService orderService;

    @Autowired
    CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        cartService.clearCart();

        cartService.addItemToCart(1L);
        cartService.addItemToCart(2L);

        orderService.placeOrder();

        cartService.addItemToCart(3L);
        cartService.addItemToCart(4L);

        orderService.placeOrder();
    }

    @Test
    void placeOrder_shouldPlaceOrder() throws Exception {
        cartService.addItemToCart(1L);
        cartService.addItemToCart(2L);

        mockMvc.perform(post("/orders")
                        .header("referer", "/orders"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(containsString("redirect:orders/")));
    }

    @Test
    void placeOrder_shouldReturnErrorIfNoItemsInOrder() throws Exception {
        mockMvc.perform(post("/orders"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("oops"));
    }

    @Test
    void findAll_shouldReturnOrdersList() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(xpath("//div[contains(@class, \"order-item\")]").nodeCount(2));
    }

    @Test
    void findById_shouldReturnOrderById() throws Exception {
        cartService.addItemToCart(1L);
        OrderResponseDTO order = orderService.placeOrder();
        assertNotNull(order);

        mockMvc.perform(get("/orders/{id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));
    }

    @Test
    void findById_shouldReturn404IfOrderNotExists() throws Exception {
        mockMvc.perform(get("/orders/{id}", -1))
                .andExpect(status().isNotFound())
                .andExpect(view().name("oops"));
    }


}
