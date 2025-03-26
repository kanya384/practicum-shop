package ru.yandex.practicum.shop.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.shop.configuration.AppConfiguration;
import ru.yandex.practicum.shop.exception.StorageException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileSystemStorageUtil implements StorageUtil {
    private final Path rootPath;

    public FileSystemStorageUtil(AppConfiguration appConfiguration) {
        this.rootPath = Paths.get(appConfiguration.getStorageRootPath());
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("failed to store empty file");
            }

            Path destinationFile = rootPath.resolve(
                            Paths.get(generateRandomFileName(file.getOriginalFilename())))
                    .normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            return destinationFile.getFileName().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeFile(String fileName) throws IOException {
        Path filePath = rootPath.resolve(fileName);
        Files.delete(filePath);
    }

    private String generateRandomFileName(String originalName) {
        return UUID.randomUUID() + "." + FilenameUtils.getExtension(originalName);
    }
}