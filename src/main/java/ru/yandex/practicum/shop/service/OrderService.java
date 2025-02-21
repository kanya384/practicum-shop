package ru.yandex.practicum.shop.service;

import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO placeOrder();

    OrderResponseDTO findById(Long id);

    List<OrderResponseDTO> findAll();
}
