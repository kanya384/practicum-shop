package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.shop.dto.product.ProductCreateDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductUpdateDTO;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.ProductMapper;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CartService cartService;

    public void save(ProductCreateDTO data) {
        var product = productMapper.map(data);
        productRepository.save(product);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductUpdateDTO data) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт", id));

        productMapper.update(data, product);

        productRepository.save(product);

        return productMapper.map(product);
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        return productRepository
                .findById(id)
                .map(productMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт", id));
    }

    @Override
    public List<ProductResponseDTO> findAll(String search, int offset, int limit) {
        if (!search.isEmpty()) {
            return productRepository
                    .findAllByTitleOrDescription(search, PageRequest.of(offset, limit))
                    .stream()
                    .map(productMapper::map)
                    .toList();
        }

        return productRepository
                .findAll(PageRequest.of(offset, limit))
                .stream()
                .map(productMapper::map)
                .toList();
    }
}
