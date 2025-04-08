package ru.yandex.practicum.shop.service.impl;

import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.shop.mapper.OrderMapperImpl;
import ru.yandex.practicum.shop.mapper.ProductMapperImpl;

@SpringBootTest(classes = {OrderServiceImpl.class, ProductMapperImpl.class, OrderMapperImpl.class})
public class OrderServiceImplTest {
    /*
    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private OrderItemRepository orderItemRepository;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private PaymentsService paymentsService;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        reset(cartService);
        reset(orderRepository);
        reset(orderItemRepository);
        reset(paymentsService);
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

        when(paymentsService.getBalance())
                .thenReturn(Mono.just(10));

        Order order = new Order(1L, LocalDate.now());
        OrderItem orderItem = new OrderItem(1L, 1L, 1L, 1);
        
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
    void placeOrder_shouldThrowMoneyError() {
        Product product = new Product(1L, "title", "description", "image", 1);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(Mono.just(new Order(1L, LocalDate.now())));

        when(cartService.getCartItems())
                .thenReturn(List.of(new CartItem(product, 1)));

        when(paymentsService.getBalance())
                .thenReturn(Mono.just(0));

        StepVerifier.create(orderService.placeOrder())
                .expectError(NotEnoughMoneyException.class);
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
    */
}
