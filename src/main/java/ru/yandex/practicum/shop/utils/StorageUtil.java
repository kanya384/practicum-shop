package ru.yandex.practicum.shop.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageUtil {
    String store(MultipartFile file);

    void removeFile(String fileName) throws IOException;
}
