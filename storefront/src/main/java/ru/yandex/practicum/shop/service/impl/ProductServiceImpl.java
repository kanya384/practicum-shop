package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.product.*;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.ProductMapper;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.ProductService;
import ru.yandex.practicum.shop.utils.StorageUtil;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CartService cartService;
    private final StorageUtil storageUtil;

    @Transactional
    @Override
    public Mono<ProductResponseDTO> save(ProductCreateDTO data) {
        return Mono.just(productMapper.mapCreate(data))
                .doOnNext(p -> {
                    String imageFileName = storageUtil.store(data.getFile());
                    p.setImage(imageFileName);
                })
                .doOnNext(productRepository::save)
                .map(productMapper::map);
    }

    @Transactional
    @Override
    public Mono<ProductResponseDTO> update(Long id, ProductUpdateDTO data) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Продукт", id)))
                .map(p -> {
                            productMapper.update(data, p);
                            if (data.getImage().isPresent()) {
                                String imageFileName = storageUtil.store(data.getImage().get());
                                p.setImage(imageFileName);
                            }
                            return p;
                        }
                )
                .flatMap(productRepository::save)
                .map(productMapper::map);
    }

    @Override
    @Cacheable(
            value = "product",
            key = "#id"
    )
    public Mono<ProductResponseDTO> findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::map)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Продукт", id)));
    }

    @Override
    @Cacheable(
            value = "products"
    )
    public Mono<ProductsPageResponseDTO> findAll(String search, int page, int pageSize, ProductSort sort) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, toDbSort(sort));


        Flux<Product> products = search != null && !search.isEmpty() ?
                this.productRepository.findAllByTitleOrDescription(search, pageable)
                : this.productRepository.findAllBy(pageable);

        Mono<Integer> count = search != null && !search.isEmpty() ?
                this.productRepository.countAllByTitleOrDescription(search)
                : this.productRepository.countAll();


        return products
                .map(productMapper::map)
                .collectList()
                .zipWith(count)
                .map(p -> ProductsPageResponseDTO.builder()
                        .list(p.getT1())
                        .page(page)
                        .totalCount(p.getT2())
                        .pageSize(pageSize)
                        .build());
    }

    private Sort toDbSort(ProductSort productSort) {
        if (productSort.equals(ProductSort.EMPTY)) {
            return Sort.unsorted();
        }

        return Sort.by(Sort.Direction.ASC, productSort.toString().toLowerCase());
    }
}
