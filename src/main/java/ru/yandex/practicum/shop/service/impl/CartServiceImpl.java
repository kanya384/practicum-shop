package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.shop.dto.cart.CartItemResponseDTO;
import ru.yandex.practicum.shop.exception.AlreadyExistsInCartException;
import ru.yandex.practicum.shop.exception.NoItemInCartException;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.CartMapper;
import ru.yandex.practicum.shop.model.Cart;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final Cart cart;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    public synchronized void addItemToCart(Long productId) {
        if (cart.containsKey(productId)) {
            throw new AlreadyExistsInCartException(String.format("Продукт c id = %d уже в корзине", productId));
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт", productId));

        cart.put(productId, new CartItem(product, 1));
    }

    public void removeItemFromCart(Long productId) {
        cart.remove(productId);
    }

    public synchronized void increaseItemCount(Long productId) {
        CartItem item = cart.get(productId);
        if (item == null) {
            throw new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId));
        }

        item.inc();
    }

    public synchronized void decreaseItemCount(Long productId) {
        CartItem item = cart.get(productId);
        if (item == null) {
            throw new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId));
        }

        item.dec();

        if (item.getCount() == 0) {
            cart.remove(productId);
        }
    }

    @Override
    public void clearCart() {
        cart.clear();
    }

    public List<CartItem> getCartItems() {
        return cart.values().stream()
                .toList();
    }

    @Override
    public Map<Long, CartItem> getProductsInCartMap() {
        return Collections.unmodifiableMap(cart);
    }

    public List<CartItemResponseDTO> returnCartItems() {
        return cart.values().stream()
                .map(cartMapper::map)
                .toList();
    }
}
