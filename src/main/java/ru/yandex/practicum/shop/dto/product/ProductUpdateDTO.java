package ru.yandex.practicum.shop.dto.product;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductUpdateDTO {
    private JsonNullable<String> title;

    private JsonNullable<String> description;

    private JsonNullable<MultipartFile> image;

    private JsonNullable<Integer> price;
}
