package ru.yandex.practicum.shop.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Setter
public class Cart extends ConcurrentHashMap<Long, CartItem> {
}