package ru.yandex.practicum.shop.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
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
