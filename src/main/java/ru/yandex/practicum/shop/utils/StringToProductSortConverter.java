package ru.yandex.practicum.shop.utils;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.shop.dto.product.ProductSort;

@Component
public class StringToProductSortConverter implements Converter<String, ProductSort> {
    @Override
    public ProductSort convert(String source) {
        if (source.isBlank()) {
            return ProductSort.EMPTY;
        }

        return ProductSort.valueOf(source.toUpperCase());
    }
}
