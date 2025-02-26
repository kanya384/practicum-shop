package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.OrderMapper;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Order;
import ru.yandex.practicum.shop.model.OrderItem;
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
            OrderItem orderItem = OrderMapper.map(cartItem);
            orderItem.setOrder(order);
            order.addItem(orderItem);
        });

        orderRepository.save(order);
        orderItemRepository.saveAll(order.getItems());

        cartService.clearCart();

        return orderMapper.map(order);
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
                .findAlOrdersWithOrderItems()
                .stream()
                .map(orderMapper::map)
                .toList();
    }

}
