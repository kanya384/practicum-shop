package ru.yandex.practicum.shop.service;

import ru.yandex.practicum.shop.dto.product.ProductCreateDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductUpdateDTO;

import java.util.List;

public interface ProductService {
    void save(ProductCreateDTO data);

    ProductResponseDTO update(Long id, ProductUpdateDTO data);

    ProductResponseDTO findById(Long id);

    List<ProductResponseDTO> findAll(String search, int offset, int limit);
}
