package ru.yandex.practicum.shop.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class ProductCreateDTO {
    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private MultipartFile image;

    @NotNull
    private Integer price;
}
