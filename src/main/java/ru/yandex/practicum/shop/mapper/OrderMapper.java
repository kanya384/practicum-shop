package ru.yandex.practicum.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.shop.dto.order.OrderItemResponseDTO;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Order;
import ru.yandex.practicum.shop.model.OrderItem;

@Mapper(
        uses = ProductMapperImpl.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public abstract class OrderMapper {
    public abstract OrderResponseDTO map(Order data);

    public abstract OrderItemResponseDTO map(OrderItem data);

    public static OrderItem map(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setCount(cartItem.getCount());
        return orderItem;
    }
}
