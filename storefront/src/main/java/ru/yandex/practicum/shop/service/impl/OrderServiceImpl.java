package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.order.OrderItemResponseDTO;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.exception.NoProductsInOrderException;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.ProductMapper;
import ru.yandex.practicum.shop.model.Order;
import ru.yandex.practicum.shop.model.OrderItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.OrderItemRepository;
import ru.yandex.practicum.shop.repository.OrderRepository;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.OrderService;
import ru.yandex.practicum.shop.service.PaymentsService;
import ru.yandex.practicum.shop.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CartService cartService;
    private final PaymentsService paymentsService;

    @Transactional
    @Override
    public Mono<OrderResponseDTO> placeOrder() {
        return cartService.getCountOfCartItems()
                .map(count -> count == 0)
                .flatMap(isEmpty -> {
                    if (isEmpty) {
                        return Mono.error(new NoProductsInOrderException("ошибка сохранения заказа"));
                    }

                    return Mono.just(isEmpty);
                })
                .flatMap(c -> cartService.getSumOfCartItems())
                .zipWith(SecurityUtils.getUserId())
                .flatMap(tuple -> {
                    var orderSum = tuple.getT1();
                    var userId = tuple.getT2();

                    return paymentsService.processPayment(userId, orderSum)
                            .thenReturn(userId);
                })
                .flatMap(userId -> orderRepository.save(new Order(userId)))
                .switchIfEmpty(Mono.error(new InternalError("ошибка сохранения заказа")))
                .flatMap(o -> storeOrderItems(o.getId()))
                .flatMap(this::findById);
    }

    @Override
    public Mono<OrderResponseDTO> findById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Заказ", id)))
                .zipWith(findOrderItemsByOrderId(id))
                .map(tuple ->
                        OrderResponseDTO.builder()
                                .id(tuple.getT1().getId())
                                .items(tuple.getT2())
                                .sum(tuple.getT2().stream()
                                        .map(OrderItemResponseDTO::getTotalPrice)
                                        .reduce(0, Integer::sum))
                                .createdAt(tuple.getT1().getCreatedAt())
                                .build()
                );
    }

    @Override
    public Flux<OrderResponseDTO> findOrdersOfUser() {
        return SecurityUtils.getUserId()
                .flatMapMany(orderRepository::findByUserId)
                .map(o -> OrderResponseDTO.builder()
                        .id(o.getId())
                        .createdAt(o.getCreatedAt())
                        .build());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private Mono<Long> storeOrderItems(Long orderId) {
        return SecurityUtils.getUserId()
                .flatMapMany(cartService::getCartItemsOfUser)
                .map(ci -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(ci.getProductId());
                    orderItem.setCount(ci.getCount());
                    orderItem.setOrderId(orderId);
                    return orderItem;
                })
                .collectList()
                .flatMapMany(orderItemRepository::saveAll)
                .collectList()
                .flatMap(c -> cartService.clearCart().thenReturn(orderId));
    }

    private Mono<List<OrderItemResponseDTO>> findOrderItemsByOrderId(Long orderId) {
        return findProductsInOrderMapByOrderId(orderId)
                .zipWith(orderItemRepository.findAllByOrderId(orderId)
                        .collectList())
                .map(tuple -> {
                            List<OrderItem> orderItems = tuple.getT2();
                            List<OrderItemResponseDTO> result = new ArrayList<>();
                            orderItems.forEach(orderItem -> {
                                ProductResponseDTO product = tuple.getT1().get(orderItem.getProductId());

                                result.add(OrderItemResponseDTO.builder()
                                        .id(orderItem.getId())
                                        .product(product)
                                        .totalPrice(product.getPrice() * orderItem.getCount())
                                        .count(orderItem.getCount())
                                        .build());
                            });


                            return result;
                        }
                );
    }

    private Mono<Map<Long, ProductResponseDTO>> findProductsInOrderMapByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId)
                .map(OrderItem::getProductId)
                .collectList()
                .map(productRepository::findAllById)
                .flatMap(p -> p.collectMap(
                        Product::getId,
                        productMapper::map
                ));
    }
}
