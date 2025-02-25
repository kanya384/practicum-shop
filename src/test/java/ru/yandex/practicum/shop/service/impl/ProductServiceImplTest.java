package ru.yandex.practicum.shop.service.impl;

import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.shop.dto.product.*;
import ru.yandex.practicum.shop.mapper.ProductMapperImpl;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.utils.StorageUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ProductServiceImpl.class, ProductMapperImpl.class, CartServiceImpl.class})
public class ProductServiceImplTest {
    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private StorageUtil storageUtil;

    @MockitoBean
    private CartServiceImpl cartService;

    @Autowired
    private ProductServiceImpl productService;

    @Test
    void save_shouldCreateProduct() {
        ProductCreateDTO dto = new ProductCreateDTO("title", "content",
                new MockMultipartFile("image", "image.jpg", "image/jpg",
                        "some image".getBytes()), 100);

        when(productRepository.save(any(Product.class)))
                .thenReturn(new Product(1L, "title", "image", "content", 100));

        productService.save(dto);

        verify(productRepository, times(1)).save(any(Product.class));
        verify(storageUtil, times(1)).store(any(MultipartFile.class));
    }

    @Test
    void update_shouldUpdatePost() {
        ProductUpdateDTO dto = new ProductUpdateDTO("new title", "new content",
                JsonNullable.of(new MockMultipartFile("image", "image.jpg", "image/jpg",
                        "some image".getBytes())), 100);

        when(productRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Product(1L, "old title", "old image", "old content", 50)));

        when(storageUtil.store(any(MultipartFile.class)))
                .thenReturn("new image");

        var productResponse = productService.update(1L, dto);

        verify(productRepository, times(1)).save(any(Product.class));
        verify(storageUtil, times(1)).store(any(MockMultipartFile.class));

        assertEquals("new title", productResponse.getTitle());
        assertEquals("new content", productResponse.getDescription());
        assertEquals("new image", productResponse.getImage());
        assertEquals(100, productResponse.getPrice());
    }

    @Test
    void update_shouldNotUpdateImageIfNull() {
        ProductUpdateDTO dto = new ProductUpdateDTO("new title", "new content",
                JsonNullable.undefined(), 100);

        when(productRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Product(1L, "old title", "old content", "old image", 50)));

        var productResponse = productService.update(1L, dto);

        verify(productRepository, times(1)).save(any(Product.class));
        verify(storageUtil, times(0)).store(any(MockMultipartFile.class));

        assertEquals("new title", productResponse.getTitle());
        assertEquals("new content", productResponse.getDescription());
        assertEquals("old image", productResponse.getImage());
        assertEquals(100, productResponse.getPrice());
    }

    @Test
    void findById_shouldReturnProductById() {
        when(productRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Product(1L, "old title", "old image", "old content", 50)));

        ProductResponseDTO product = productService.findById(1L);

        assertNotNull(product);
    }

    @Test
    void findById_shouldThrowExceptionIfNotFounded() {
        when(productRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Product(1L, "old title", "old image", "old content", 50)));

        ProductResponseDTO product = productService.findById(1L);

        assertNotNull(product);
    }

    @Test
    void findAll_shouldReturnProductsListIfSearchIsEmpty() {
        Product product1 = new Product(1L, "old title", "old image", "old content", 50);
        Product product2 = new Product(2L, "old title", "old image", "old content", 50);

        when(productRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(product1, product2)));

        when(productRepository.countAll()).thenReturn(2);

        when(cartService.getProductsInCartMap())
                .thenReturn(Map.of(
                        1L, new CartItem(product1, 1))
                );

        ProductsPageResponseDTO products = productService.findAll("", 10, 11, ProductSort.EMPTY);

        assertEquals(2, products.getTotalCount());

        ProductResponseDTO product1Result = products.getList()
                .stream()
                .filter(pr -> pr.getId() == 1)
                .findFirst()
                .orElse(null);

        assertNotNull(product1Result);
        assertEquals(1, product1Result.getCount());
    }

    @Test
    void findAll_shouldReturnProductsListIfSearchIsNotEmpty() {
        Product product1 = new Product(1L, "old title", "old image", "old content", 50);
        Product product2 = new Product(2L, "old title", "old image", "old content", 50);

        when(productRepository.findAllByTitleOrDescription(any(String.class), any(PageRequest.class)))
                .thenReturn(List.of(product1,
                        product2));

        when(productRepository.countAllByTitleOrDescription(any(String.class))).thenReturn(2);

        when(cartService.getProductsInCartMap())
                .thenReturn(Map.of(
                        1L, new CartItem(product1, 1))
                );

        ProductsPageResponseDTO products = productService.findAll("search", 10, 11, ProductSort.EMPTY);

        assertEquals(2, products.getTotalCount());

        ProductResponseDTO product1Result = products.getList()
                .stream()
                .filter(pr -> pr.getId() == 1)
                .findFirst()
                .orElse(null);

        assertNotNull(product1Result);
        assertEquals(1, product1Result.getCount());
    }

}
