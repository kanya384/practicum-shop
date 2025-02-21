package ru.yandex.practicum.shop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class Cart extends ConcurrentHashMap<Long, CartItem> {
}