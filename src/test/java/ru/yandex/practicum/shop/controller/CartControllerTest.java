package ru.yandex.practicum.shop.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(CartController.class)
public class CartControllerTest {
    /*@Autowired
    MockMvc mockMvc;

    @MockitoBean
    CartService cartService;

    @Test
    void getCartItems_shouldReturnProductsList() throws Exception {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);
        productResponseDTO.setTitle("title");
        productResponseDTO.setDescription("description");
        productResponseDTO.setImage("image");
        productResponseDTO.setPrice(100);
        productResponseDTO.setCount(0);

        when(cartService.returnCartItems())
                .thenReturn(List.of(new CartItemResponseDTO(productResponseDTO, 1)));

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("cart"))
                .andExpect(xpath("//div[contains(@class, \"cart-item\")]").nodeCount(1));
    }

    @Test
    void addProductToCart_shouldAddProductToCart() throws Exception {
        mockMvc.perform(post("/cart/add/{id}", 3L)
                        .header("referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        verify(cartService, times(1))
                .addItemToCart(any());
    }

    @Test
    void addProductToCart_shouldReturnErrorIfProductAlreadyExistingInCart() throws Exception {
        doThrow(new AlreadyExistsInCartException("")).when(cartService).addItemToCart(anyLong());

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

        verify(cartService, times(1))
                .removeItemFromCart(any());
    }

    @Test
    void removeProductFromCart_shouldReturnErrorIfProductNotExistingInCart() throws Exception {
        doThrow(new ResourceNotFoundException("Продутк", 3L)).when(cartService).removeItemFromCart(anyLong());

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

        verify(cartService, times(1))
                .increaseItemCount(any());
    }

    @Test
    void increaseItemCount_shouldReturnNotFoundIfNoItemInCart() throws Exception {
        doThrow(new ResourceNotFoundException("Продутк", 3L)).when(cartService).increaseItemCount(anyLong());

        mockMvc.perform(post("/cart/inc/{id}", 3L)
                        .header("referer", "/cart"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("oops"));
    }

    @Test
    void decreaseItemCount_shouldIncreaseItemCount() throws Exception {
        mockMvc.perform(post("/cart/dec/{id}", 1L)
                        .header("referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        verify(cartService, times(1))
                .decreaseItemCount(any());
    }

    @Test
    void decreaseItemCount_shouldReturnNotFoundIfNoItemInCart() throws Exception {
        doThrow(new ResourceNotFoundException("Продутк", 3L)).when(cartService).decreaseItemCount(anyLong());

        mockMvc.perform(post("/cart/dec/{id}", 3L)
                        .header("referer", "/cart"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("oops"));
    }*/
}
