package ru.yandex.practicum.shop.mapper;

import org.mapstruct.*;
import ru.yandex.practicum.shop.dto.product.ProductCreateDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductUpdateDTO;
import ru.yandex.practicum.shop.model.Product;

@Mapper(
        uses = {JsonNullableMapperImpl.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProductMapper {
    @Mapping(target = "image", ignore = true)
    public abstract Product map(ProductCreateDTO data);
    
    public abstract ProductResponseDTO map(Product product);

    @Mapping(target = "image", ignore = true)
    public abstract void update(ProductUpdateDTO data, @MappingTarget Product product);
}
