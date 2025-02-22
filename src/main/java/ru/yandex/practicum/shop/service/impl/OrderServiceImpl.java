package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.OrderMapper;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Order;
import ru.yandex.practicum.shop.repository.OrderItemRepository;
import ru.yandex.practicum.shop.repository.OrderRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    @Transactional
    @Override
    public OrderResponseDTO placeOrder() {
        List<CartItem> cartItems = cartService.getCartItems();

        Order order = new Order();
        cartItems.forEach(cartItem -> {
            order.addItem(orderMapper.map(cartItem));
        });

        var savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(savedOrder.getItems());

        cartService.clearCart();
        
        return orderMapper.map(savedOrder);
    }

    @Override
    public OrderResponseDTO findById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ", id));
    }

    @Override
    public List<OrderResponseDTO> findAll() {
        return orderRepository
                .findAll()
                .stream()
                .map(orderMapper::map)
                .toList();
    }
}
