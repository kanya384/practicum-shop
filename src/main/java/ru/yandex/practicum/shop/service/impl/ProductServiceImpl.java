package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.shop.dto.product.ProductCreateDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductUpdateDTO;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.ProductMapper;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.ProductService;
import ru.yandex.practicum.shop.utils.StorageUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final StorageUtil storageUtil;

    @Transactional
    public void save(ProductCreateDTO data) {
        var product = productMapper.map(data);
        String imageFileName = storageUtil.store(data.getFile());
        product.setImage(imageFileName);

        productRepository.save(product);
    }

    @Transactional
    @Override
    public ProductResponseDTO update(Long id, ProductUpdateDTO data) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт", id));

        productMapper.update(data, product);

        if (data.getImage().isPresent()) {
            String imageFileName = storageUtil.store(data.getImage().get());
            product.setImage(imageFileName);
        }

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
