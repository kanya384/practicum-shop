package ru.yandex.practicum.shop;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.annotation.DirtiesContext;

@AutoConfigureWebTestClient
@DirtiesContext
@Tag("integration")
public class CartControllerIntegrationTest extends AbstractTestContainer {
    /*@Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService.clearCart();
        cartService.addItemToCart(1L)
                .flatMap(c -> cartService.addItemToCart(2L))
                .block();
    }

    @Test
    void getCartItems_shouldReturnProductsList() {
        webTestClient.get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<div class=\"cart-item"));
                });
    }

    @Test
    void addProductToCart_shouldAddProductToCart() {
        webTestClient.post()
                .uri("/cart/add/{id}", 3L)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void addProductToCart_shouldReturnErrorIfProductAlreadyExistingInCart() {
        webTestClient.post()
                .uri("/cart/add/{id}", 2L)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<h1>Упс, что-то пошло не так!</h1>"));
                });
    }

    @Test
    void removeProductFromCart_shouldRemoveProductFromCart() {
        webTestClient.post()
                .uri("/cart/remove/{id}", 2L)
                .exchange()
                .expectStatus().isOk();

        assertNull(cartService.getCartItems()
                .stream()
                .filter(cart -> cart.getProduct().getId() == 2L)
                .findFirst()
                .orElse(null));
    }

    @Test
    void removeProductFromCart_shouldReturnErrorIfProductNotExistingInCart() {
        webTestClient.post()
                .uri("/cart/remove/{id}", -1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<h1>Упс, что-то пошло не так!</h1>"));
                });
    }

    @Test
    void increaseItemCount_shouldIncreaseItemCount() {
        webTestClient.post()
                .uri("/cart/inc/{id}", 1)
                .exchange()
                .expectStatus().isOk();

        CartItem cartItem = cartService.getCartItems()
                .stream()
                .filter(cart -> cart.getProduct().getId() == 1L)
                .findFirst()
                .orElse(null);

        assertNotNull(cartItem);
        assertEquals(2, cartItem.getCount());
    }

    @Test
    void increaseItemCount_shouldReturnNotFoundIfNoItemInCart() {
        webTestClient.post()
                .uri("/cart/inc/{id}", -1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void decreaseItemCount_shouldIncreaseItemCount() {
        assertEquals(3, cartService.increaseItemCount(1L)
                .flatMap(c -> cartService.increaseItemCount(1L))
                .flatMap(c -> cartService.getCartItemById(1L))
                .map(CartItem::getCount).block());

        webTestClient.post()
                .uri("/cart/dec/{id}", 1)
                .exchange()
                .expectStatus().isOk();

        CartItem cartItem = cartService.getCartItems()
                .stream()
                .filter(cart -> cart.getProduct().getId() == 1L)
                .findFirst()
                .orElse(null);

        assertNotNull(cartItem);
        assertEquals(2, cartItem.getCount());
    }

    @Test
    void decreaseItemCount_shouldReturnNotFoundIfNoItemInCart() {
        webTestClient.post()
                .uri("/cart/dec/{id}", 3L)
                .exchange()
                .expectStatus().isNotFound();
    }
    */
}
