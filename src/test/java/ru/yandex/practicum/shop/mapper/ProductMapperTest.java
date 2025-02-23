package ru.yandex.practicum.shop.mapper;

import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import ru.yandex.practicum.shop.dto.product.ProductCreateDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductUpdateDTO;
import ru.yandex.practicum.shop.model.Product;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ProductMapperImpl.class})
public class ProductMapperTest {
    @Autowired
    private ProductMapper productMapper;

    @Test
    void map_shouldMapProductCreateDTOToProduct() {
        var data = new ProductCreateDTO("title", "description", new MockMultipartFile("image", "image.jpg", "image/jpg",
                "some image".getBytes()), 100);

        Product product = productMapper.map(data);

        assertNotNull(product);
        assertNull(product.getId());
        assertEquals("title", product.getTitle());
        assertEquals("description", product.getDescription());
        assertEquals(100, product.getPrice());
    }

    @Test
    void map_shouldMapProductToProductResponseDTO() {
        var product = new Product(1L, "title", "description", "image", 100);

        ProductResponseDTO responseDTO = productMapper.map(product);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("title", responseDTO.getTitle());
        assertEquals("description", responseDTO.getDescription());
        assertEquals(100, responseDTO.getPrice());
    }

    @Test
    void update_shouldUpdateProductFields() {
        var updateData = new ProductUpdateDTO("new title", "new description", JsonNullable.of(new MockMultipartFile("image", "image.jpg", "image/jpg",
                "some image".getBytes())), 200);

        var product = new Product(1L, "title", "description", "image", 100);

        productMapper.update(updateData, product);

        assertEquals("new title", product.getTitle());
        assertEquals("new description", product.getDescription());
        assertEquals(200, product.getPrice());
    }
}
