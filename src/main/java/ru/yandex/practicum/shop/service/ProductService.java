package ru.yandex.practicum.shop.service;

import ru.yandex.practicum.shop.dto.product.*;

public interface ProductService {
    void save(ProductCreateDTO data);

    ProductResponseDTO update(Long id, ProductUpdateDTO data);

    ProductResponseDTO findById(Long id);

    ProductsPageResponseDTO findAll(String search, int page, int pageSize, ProductSort sort);
}
