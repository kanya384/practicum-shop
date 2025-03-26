package ru.yandex.practicum.shop.utils;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.shop.configuration.AppConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {FileSystemStorageUtil.class, AppConfiguration.class})
@ActiveProfiles("test")
public class FileSystemStorageUtilTest {
    @Autowired
    private AppConfiguration appConfiguration;

    @Autowired
    private FileSystemStorageUtil fileSystemStorageUtil;

    @AfterEach
    void cleanPath() {
        try {
            FileUtils.cleanDirectory(new File(appConfiguration.getStorageRootPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void store_shouldSaveFile() {
        String fileName = fileSystemStorageUtil.store(new MockMultipartFile("image", "image.jpg", "image/jpg",
                "some image".getBytes()));

        assertTrue(Files.exists(Path.of(appConfiguration.getStorageRootPath() + "/" + fileName)));
    }

    @Test
    void remove_shouldRemoveFile() {
        String fileName = fileSystemStorageUtil.store(new MockMultipartFile("image", "image.jpg", "image/jpg",
                "some image".getBytes()));

        assertTrue(Files.exists(Path.of(appConfiguration.getStorageRootPath() + "/" + fileName)));

        assertDoesNotThrow(() -> fileSystemStorageUtil.removeFile(fileName));

        assertFalse(Files.exists(Path.of(appConfiguration.getStorageRootPath() + "/" + fileName)));
    }
}
