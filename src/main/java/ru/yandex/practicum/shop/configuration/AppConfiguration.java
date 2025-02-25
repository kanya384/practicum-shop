package ru.yandex.practicum.shop.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
public class AppConfiguration {
    @Value("${storage-path}")
    private String storageRootPath;
}
