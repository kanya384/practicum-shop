package ru.yandex.practicum.shop.utils;

import org.springframework.web.multipart.MultipartFile;

public interface StorageUtil {
    String store(MultipartFile file);
}
