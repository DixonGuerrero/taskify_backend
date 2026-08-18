package com.taskify.taskifyApi.infrastructure.output.adapters;

import com.taskify.taskifyApi.infrastructure.config.LocalStorageProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class LocalStorageAdapterTest {

    @TempDir
    Path tempDir;

    LocalStorageAdapter adapter;

    @BeforeEach
    void setUp() {
        LocalStorageProperties properties = new LocalStorageProperties();
        properties.setDirectory(tempDir.toString());
        properties.setBaseUrl("http://localhost:8080/uploads");

        adapter = new LocalStorageAdapter(properties);
    }

    @Test
    void uploadFile_writesFileToDiskAndReturnsGeneratedKey() throws Exception {
        byte[] content = "hello world".getBytes();

        String key = adapter.uploadFile(new ByteArrayInputStream(content), "photo.png", "image/png");

        assertThat(key).endsWith(".png");
        assertThat(Files.readAllBytes(tempDir.resolve(key))).isEqualTo(content);
    }

    @Test
    void getFileUrl_buildsUrlFromBaseUrlAndKey() {
        String url = adapter.getFileUrl("some-key.png");

        assertThat(url).isEqualTo("http://localhost:8080/uploads/some-key.png");
    }

    @Test
    void deleteFile_removesExistingFile() throws Exception {
        String key = adapter.uploadFile(new ByteArrayInputStream("data".getBytes()), "doc.pdf", "application/pdf");
        assertThat(Files.exists(tempDir.resolve(key))).isTrue();

        adapter.deleteFile(key);

        assertThat(Files.exists(tempDir.resolve(key))).isFalse();
    }

    @Test
    void deleteFile_doesNotThrowWhenFileIsMissing() {
        adapter.deleteFile("does-not-exist.png");
    }
}
