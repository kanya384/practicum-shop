package ru.yandex.practicum.shop.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductsPageResponseDTO {
    private List<ProductResponseDTO> list;
    private int page;
    private int pageSize;
    private int totalCount;

    public boolean hasNextPage() {
        return (totalCount - page * pageSize) > 0;
    }

    public boolean hasPrevPage() {
        return page > 1;
    }
}
