package com.taskify.taskifyApi.application.ports.output;

import java.io.IOException;
import java.io.InputStream;

public interface FileStoragePort {
    String uploadFile(InputStream fileStream, String fileName, String contentType) throws IOException;
    String getFileUrl(String storageKey) throws Exception;
    void deleteFile(String storageKey);
}