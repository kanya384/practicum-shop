package ru.yandex.practicum.shop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductSort;
import ru.yandex.practicum.shop.dto.product.ProductsPageResponseDTO;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.service.ProductService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
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
    }
}
