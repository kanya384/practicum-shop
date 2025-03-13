package ru.yandex.practicum.shop.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    /*@Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @Test
    void productsList_shouldReturnProductsList() throws Exception {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);
        productResponseDTO.setTitle("title");
        productResponseDTO.setDescription("description");
        productResponseDTO.setImage("image");
        productResponseDTO.setPrice(100);

        ProductsPageResponseDTO productsPageResponseDTO = new ProductsPageResponseDTO();
        productsPageResponseDTO.setPage(1);
        productsPageResponseDTO.setPageSize(10);
        productsPageResponseDTO.setTotalCount(100);
        productsPageResponseDTO.setList(List.of(productResponseDTO));

        when(productService.findAll(null, 1, 10, ProductSort.EMPTY))
                .thenReturn(productsPageResponseDTO);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("products"))
                .andExpect(xpath("//div[contains(@class, \"product\")]").nodeCount(1));
    }

    @Test
    void findById_shouldReturnProductById() throws Exception {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);
        productResponseDTO.setTitle("title");
        productResponseDTO.setDescription("description");
        productResponseDTO.setImage("image");
        productResponseDTO.setPrice(100);

        when(productService.findById(1L))
                .thenReturn(productResponseDTO);

        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("product"));
    }

    @Test
    void findById_shouldReturn404Page() throws Exception {
        when(productService.findById(any()))
                .thenThrow(new ResourceNotFoundException("Продукт", -1L));

        mockMvc.perform(get("/products/{id}", -1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }*/
}
