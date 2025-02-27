package ru.yandex.practicum.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.service.CartService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CartControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    CartService cartService;

    @BeforeEach
    void setUp() {
        cartService.clearCart();
        cartService.addItemToCart(1L);
        cartService.addItemToCart(2L);
    }

    @Test
    void getCartItems_shouldReturnProductsList() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("cart"))
                .andExpect(xpath("//div[contains(@class, \"cart-item\")]").nodeCount(2));
    }

    @Test
    void addProductToCart_shouldAddProductToCart() throws Exception {
        mockMvc.perform(post("/cart/add/{id}", 3L)
                        .header("referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        assertNotNull(cartService.getCartItems()
                .stream()
                .filter(cart -> cart.getProduct().getId() == 3L)
                .findFirst()
                .orElse(null));
    }

    @Test
    void addProductToCart_shouldReturnErrorIfProductAlreadyExistingInCart() throws Exception {
        mockMvc.perform(post("/cart/add/{id}", 2L)
                        .header("referer", "/cart"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("oops"));
    }

    @Test
    void removeProductFromCart_shouldRemoveProductFromCart() throws Exception {
        mockMvc.perform(post("/cart/remove/{id}", 2L)
                        .header("referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        assertNull(cartService.getCartItems()
                .stream()
                .filter(cart -> cart.getProduct().getId() == 2L)
                .findFirst()
                .orElse(null));
    }

    @Test
    void removeProductFromCart_shouldReturnErrorIfProductNotExistingInCart() throws Exception {
        mockMvc.perform(post("/cart/remove/{id}", 3L)
                        .header("referer", "/cart"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("oops"));
    }

    @Test
    void increaseItemCount_shouldIncreaseItemCount() throws Exception {
        mockMvc.perform(post("/cart/inc/{id}", 1L)
                        .header("referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        CartItem cartItem = cartService.getCartItems()
                .stream()
                .filter(cart -> cart.getProduct().getId() == 1L)
                .findFirst()
                .orElse(null);

        assertNotNull(cartItem);
        assertEquals(2, cartItem.getCount());
    }

    @Test
    void increaseItemCount_shouldReturnNotFoundIfNoItemInCart() throws Exception {
        mockMvc.perform(post("/cart/inc/{id}", 3L)
                        .header("referer", "/cart"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("oops"));
    }

    @Test
    void decreaseItemCount_shouldIncreaseItemCount() throws Exception {
        cartService.increaseItemCount(1L);
        cartService.increaseItemCount(1L);
        assertEquals(3, cartService.getCartItemById(1L).getCount());

        mockMvc.perform(post("/cart/dec/{id}", 1L)
                        .header("referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        CartItem cartItem = cartService.getCartItems()
                .stream()
                .filter(cart -> cart.getProduct().getId() == 1L)
                .findFirst()
                .orElse(null);

        assertNotNull(cartItem);
        assertEquals(2, cartItem.getCount());
    }

    @Test
    void decreaseItemCount_shouldReturnNotFoundIfNoItemInCart() throws Exception {
        mockMvc.perform(post("/cart/dec/{id}", 3L)
                        .header("referer", "/cart"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("oops"));
    }
}
