package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.shop.dto.product.*;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.ProductMapper;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.ProductService;
import ru.yandex.practicum.shop.utils.StorageUtil;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CartService cartService;
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
        ProductResponseDTO product = productRepository
                .findById(id)
                .map(productMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт", id));

        CartItem cartItem = cartService.getCartItemById(id);

        if (cartItem != null) {
            product.setCount(cartItem.getCount());
        }
        
        return product;
    }

    @Override
    public ProductsPageResponseDTO findAll(String search, int page, int pageSize, ProductSort productSort) {
        ProductsPageResponseDTO result = new ProductsPageResponseDTO();
        result.setPage(page);
        result.setPageSize(pageSize);

        Map<Long, CartItem> productsInCartMap = cartService.getProductsInCartMap();

        if (search != null && !search.isEmpty()) {
            result.setList(setCountOfProductsInCart(productRepository
                    .findAllByTitleOrDescription(search, PageRequest.of(page - 1, pageSize, toDbSort(productSort)))
                    .stream()
                    .map(productMapper::map)
                    .toList(), productsInCartMap));
            result.setTotalCount(productRepository.countAllByTitleOrDescription(search));
        } else {
            result.setList(setCountOfProductsInCart(productRepository
                    .findAll(PageRequest.of(page - 1, pageSize, toDbSort(productSort)))
                    .stream()
                    .map(productMapper::map)
                    .toList(), productsInCartMap));
            result.setTotalCount(productRepository.countAll());
        }

        return result;
    }

    private Sort toDbSort(ProductSort productSort) {
        if (productSort.equals(ProductSort.EMPTY)) {
            return Sort.unsorted();
        }

        return Sort.by(Sort.Direction.ASC, productSort.toString().toLowerCase());
    }

    private List<ProductResponseDTO> setCountOfProductsInCart(List<ProductResponseDTO> list, Map<Long, CartItem> cartItems) {
        list.forEach((product) -> {
            CartItem cartItem = cartItems.get(product.getId());
            if (cartItem == null) {
                return;
            }

            product.setCount(cartItem.getCount());
        });

        return list;
    }
}
