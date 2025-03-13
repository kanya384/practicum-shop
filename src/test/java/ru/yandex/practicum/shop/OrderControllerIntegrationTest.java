package ru.yandex.practicum.shop;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {
    /*@Autowired
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
    }*/


}
