package ru.yandex.practicum.shop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.shop.exception.AlreadyExistsInCartException;
import ru.yandex.practicum.shop.exception.NoItemInCartException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CartItem {
        private Product product;

        private Integer count;

        public Integer getTotalPrice() {
            return product.getPrice() * count;
        }

        public void inc() {
            count++;
        }

        public void dec() {
            count--;
        }
    }

    private final Map<Long, CartItem> items;

    public Cart() {
        this.items = new HashMap<>();
    }

    public void addItemToCart(Product product) {
        if (items.containsKey(product.getId())) {
            throw new AlreadyExistsInCartException(String.format("Продукт c id = %d уже в корзине", product.getId()));
        }

        items.put(product.getId(), new CartItem(product, 1));
    }

    public void removeItemFromCart(Product product) {
        items.remove(product.getId());
    }

    public void increaseItemCount(Long productId) {
        CartItem item = items.get(productId);
        if (item == null) {
            throw new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId));
        }

        item.inc();
    }

    public void decreaseItemCount(Long productId) {
        CartItem item = items.get(productId);
        if (item == null) {
            throw new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId));
        }

        item.dec();

        if (item.getCount() == 1) {
            items.remove(productId);
        }
    }

    public List<CartItem> cartItems() {
        return items.values().stream().toList();
    }
}