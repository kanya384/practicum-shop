package ru.yandex.practicum.shop.service;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.product.*;

public interface ProductService {
    Mono<ProductResponseDTO> save(ProductCreateDTO data);

    Mono<ProductResponseDTO> update(Long id, ProductUpdateDTO data);

    Mono<ProductResponseDTO> findById(Long id);

    Mono<ProductsPageResponseDTO> findAll(String search, int page, int pageSize, ProductSort sort);
}
