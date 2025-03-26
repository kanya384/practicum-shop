package ru.yandex.practicum.shop.service.impl;

import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.shop.dto.product.ProductCreateDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductSort;
import ru.yandex.practicum.shop.dto.product.ProductUpdateDTO;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.ProductMapper;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.utils.StorageUtil;

import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ProductServiceImpl.class, CartServiceImpl.class})
public class ProductServiceImplTest {
    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private StorageUtil storageUtil;

    @MockitoBean
    private CartServiceImpl cartService;

    @MockitoBean
    private ProductMapper productMapper;

    @Autowired
    private ProductServiceImpl productService;

    @Test
    void save_shouldCreateProduct() {
        ProductCreateDTO dto = new ProductCreateDTO("title", "content",
                new MockMultipartFile("image", "image.jpg", "image/jpg",
                        "some image".getBytes()), 100);

        var product = new Product(1L, "title", "image", "content", 100);
        var productResponse = ProductResponseDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .image(product.getImage())
                .price(product.getPrice())
                .build();

        when(productMapper.mapCreate(any(ProductCreateDTO.class)))
                .thenReturn(product);

        when(productRepository.save(any(Product.class)))
                .thenReturn(Mono.just(product));

        when(productMapper.map(any(Product.class)))
                .thenReturn(productResponse);

        Mono<ProductResponseDTO> result = productService.save(dto);

        StepVerifier.create(result)
                .expectNext(productResponse)
                .verifyComplete();

        verify(productMapper, times(1)).mapCreate(any(ProductCreateDTO.class));
        verify(productMapper, times(1)).map(any(Product.class));
        verify(productRepository, times(1)).save(any(Product.class));
        verify(storageUtil, times(1)).store(any(MultipartFile.class));
    }

    @Test
    void update_shouldUpdatePost() {
        ProductUpdateDTO dto = new ProductUpdateDTO("new title", "new content",
                JsonNullable.of(new MockMultipartFile("image", "image.jpg", "image/jpg",
                        "some image".getBytes())), 100);

        var product = new Product(1L, "title", "image", "content", 100);
        var productResponse = ProductResponseDTO.builder()
                .id(product.getId())
                .title("new title")
                .description("new content")
                .image("new image")
                .price(100)
                .build();

        when(productMapper.map(any(Product.class)))
                .thenReturn(productResponse);

        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.just(new Product(1L, "old title", "old image", "old content", 50)));

        when(storageUtil.store(any(MultipartFile.class)))
                .thenReturn("new image");

        when(productRepository.save(any(Product.class)))
                .thenReturn(Mono.just(product));

        Mono<ProductResponseDTO> result = productService.update(1L, dto);

        StepVerifier.create(result)
                .expectNext(productResponse)
                .verifyComplete();

        verify(productRepository, times(1)).save(any(Product.class));
        verify(storageUtil, times(1)).store(any(MockMultipartFile.class));
    }

    @Test
    void update_shouldNotUpdateImageIfNull() {
        ProductUpdateDTO dto = new ProductUpdateDTO("new title", "new content",
                JsonNullable.undefined(), 100);

        var product = new Product(1L, "title", "image", "content", 100);
        var productResponse = ProductResponseDTO.builder()
                .id(product.getId())
                .title("new title")
                .description("new content")
                .image("old image")
                .price(100)
                .build();

        when(productMapper.map(any(Product.class)))
                .thenReturn(productResponse);

        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.just(new Product(1L, "old title", "old content", "old image", 50)));

        when(productRepository.save(any(Product.class)))
                .thenReturn(Mono.just(new Product(1L, "new title", "new content", "old image", 100)));

        Mono<ProductResponseDTO> result = productService.update(1L, dto);

        StepVerifier.create(result)
                .expectNext(productResponse)
                .verifyComplete();

        verify(productRepository, times(1)).save(any(Product.class));
        verify(storageUtil, times(0)).store(any(MockMultipartFile.class));
    }

    @Test
    void update_shouldThrowExceptionIfNotFounded() {
        ProductUpdateDTO dto = new ProductUpdateDTO("new title", "new content",
                JsonNullable.undefined(), 100);

        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.empty());

        var result = productService.update(1L, dto);

        StepVerifier.create(result)
                .expectError(ResourceNotFoundException.class)
                .verify();

        verify(productRepository, times(0)).save(any(Product.class));
        verify(storageUtil, times(0)).store(any(MockMultipartFile.class));
    }

    @Test
    void findById_shouldReturnProductById() {
        var product = new Product(1L, "title", "image", "content", 100);
        var productResponse = ProductResponseDTO.builder()
                .id(product.getId())
                .title("new title")
                .description("new content")
                .image("old image")
                .price(100)
                .build();

        when(cartService.getCartItemById(any(Long.class)))
                .thenReturn(Mono.just(new CartItem(product, 1)));


        when(productMapper.map(any(Product.class)))
                .thenReturn(productResponse);

        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.just(new Product(1L, "old title", "old content", "old image", 50)));

        var result = productService.findById(1L);

        StepVerifier.create(result)
                .expectNext(productResponse)
                .verifyComplete();
    }

    @Test
    void findById_shouldThrowExceptionIfNotFounded() {
        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.empty());

        when(cartService.getCartItemById(any(Long.class)))
                .thenReturn(Mono.empty());

        var result = productService.findById(1L);

        StepVerifier.create(result)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void findAll_shouldReturnProductsListIfSearchIsEmpty() {
        Product product1 = new Product(1L, "old title", "old image", "old content", 50);
        Product product2 = new Product(2L, "old title", "old image", "old content", 50);

        var productResponse = ProductResponseDTO.builder()
                .id(product1.getId())
                .title(product1.getTitle())
                .description(product1.getDescription())
                .image(product1.getImage())
                .price(product1.getPrice())
                .build();

        when(productMapper.map(any(Product.class)))
                .thenReturn(productResponse);

        when(productRepository.findAllBy(any(Pageable.class)))
                .thenReturn(Flux.just(product1, product2));

        when(productRepository.countAll())
                .thenReturn(Mono.just(2));

        when(cartService.getProductsInCartMap())
                .thenReturn(Map.of(
                        1L, new CartItem(product1, 1))
                );

        var result = productService.findAll(null, 1, 10, ProductSort.EMPTY);

        StepVerifier
                .create(result)
                .expectNextMatches(p -> p.getTotalCount() == 2)
                .verifyComplete();
    }

    @Test
    void findAll_shouldReturnProductsListIfSearchIsNotEmpty() {
        Product product1 = new Product(1L, "old title", "old image", "old content", 50);
        Product product2 = new Product(2L, "old title", "old image", "old content", 50);

        var productResponse = ProductResponseDTO.builder()
                .id(product1.getId())
                .title(product1.getTitle())
                .description(product1.getDescription())
                .image(product1.getImage())
                .price(product1.getPrice())
                .build();

        when(productMapper.map(any(Product.class)))
                .thenReturn(productResponse);

        when(productRepository.findAllByTitleOrDescription(any(String.class), any(Pageable.class)))
                .thenReturn(Flux.just(product1,
                        product2));

        when(productRepository.countAllByTitleOrDescription(any(String.class))).thenReturn(Mono.just(2));

        when(cartService.getProductsInCartMap())
                .thenReturn(Map.of(
                        1L, new CartItem(product1, 1))
                );

        var result = productService.findAll("search", 10, 11, ProductSort.EMPTY);

        StepVerifier
                .create(result)
                .expectNextMatches(p -> p.getTotalCount() == 2)
                .verifyComplete();
    }

}
