package ru.yandex.practicum.shop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class ProductUpdateDTO {
    private JsonNullable<String> title;

    private JsonNullable<String> description;

    private JsonNullable<MultipartFile> image;

    private JsonNullable<Integer> price;
}
