package ru.yandex.practicum.shop.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductsPageResponseDTO;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@Setter
@Getter
public class AppConfiguration {
    @Value("${storage-path}")
    private String storageRootPath;

    @Bean
    public RedisCacheManagerBuilderCustomizer productsListCacheCustomizer() {
        return builder -> builder.withCacheConfiguration(
                "products",                                         // Имя кеша
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.of(1, ChronoUnit.MINUTES))
                        .serializeValuesWith(                          // Сериализация JSON
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        new Jackson2JsonRedisSerializer<>(ProductsPageResponseDTO.class)
                                )
                        )
        );
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer productCacheCustomizer() {
        return builder -> builder.withCacheConfiguration(
                "product",                                         // Имя кеша
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.of(1, ChronoUnit.MINUTES))
                        .serializeValuesWith(                          // Сериализация JSON
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        new Jackson2JsonRedisSerializer<>(ProductResponseDTO.class)
                                )
                        )
        );
    }
}
