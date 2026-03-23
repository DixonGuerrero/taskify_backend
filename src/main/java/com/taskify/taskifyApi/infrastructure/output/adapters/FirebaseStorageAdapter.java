package com.taskify.taskifyApi.infrastructure.output.adapters;

import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import com.taskify.taskifyApi.application.ports.output.FileStoragePort;
import com.taskify.taskifyApi.domain.exception.image.ImageNotFoundException;
import com.taskify.taskifyApi.domain.exception.image.ImageUrlInvalidException;
import com.taskify.taskifyApi.infrastructure.config.FirebaseProperties;
import com.taskify.taskifyApi.infrastructure.output.exceptions.StorageAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Adaptador de almacenamiento de archivos que implementa la interfaz {@link FileStoragePort}
 * para interactuar con Firebase Storage.
 *
 * <p>Esta clase es responsable de abstraer la lógica de subida y eliminación de archivos
 * (especialmente imágenes) hacia y desde Firebase Storage, proporcionando una interfaz
 * limpia y desacoplada para el resto de la aplicación.</p>
 *
 * <p>Utiliza las propiedades de configuración de Firebase (inyectadas a través de {@code firebaseProperties})
 * y el cliente de Storage de Firebase Admin SDK para realizar las operaciones.</p>
 *
 * <p>Es un componente de Spring ({@code @Component}) y se beneficia de la inyección de dependencias
 * a través de Lombok's {@code @RequiredArgsConstructor} para el {@code FirebaseProperties}.</p>
 *
 * @author Dixon Guerrero
 * @version 1.0
 * @since 2025-04-04
 * @see FileStoragePort
 * @see FirebaseProperties
 * @see com.google.cloud.storage.Storage
 * @see com.google.firebase.cloud.StorageClient
 */
@Component
@ConditionalOnProperty(name = "storage.type", havingValue = "firebase")
@RequiredArgsConstructor
public class FirebaseStorageAdapter implements FileStoragePort {

    private final FirebaseProperties firebaseProperties;

    private static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media";

    /**
     * Sube un archivo (imagen) a Firebase Storage.
     * Genera un nombre de archivo único para evitar conflictos y organiza las imágenes
     * en el prefijo "images/".
     *
     * @param fileStream El InputStream del archivo a subir. Se recomienda cerrarlo después de usarlo si se maneja externamente.
     * @param fileName El nombre original del archivo (e.g., "mi_imagen.jpg").
     * @param contentType El tipo MIME del archivo (e.g., "image/jpeg", "image/png").
     * @return La URL pública de la imagen subida en Firebase Storage.
     * @throws StorageAccessException Sí ocurre un error al intentar acceder o interactuar con
     * Firebase Storage (ej., problemas de red, permisos, bucket incorrecto).
     */
    @Override
    public String uploadFile(InputStream fileStream, String fileName, String contentType) throws StorageAccessException{
        String uniqueFileName = "images/" + UUID.randomUUID() + "_" + fileName;
        try {
            Bucket bucket = StorageClient.getInstance().bucket(firebaseProperties.getStorageBucket());
            bucket.create(uniqueFileName, fileStream, contentType);
            return getImageUrl(uniqueFileName);
        } catch (StorageException e) {
            throw new StorageAccessException("Bucket: " + firebaseProperties.getStorageBucket(), "Path: " + uniqueFileName, e.getMessage());
        }
    }

    @Override
    public String getFileUrl(String storageKey) throws Exception {
        return getImageUrl(storageKey);
    }

    /**
     * Elimina un archivo (imagen) de Firebase Storage utilizando su URL pública.
     * Este método extrae la ruta del archivo de la URL y luego procede a eliminar el blob
     * correspondiente en el bucket de Firebase.
     *
     * @param fileUrl La URL pública de la imagen en Firebase Storage que se desea eliminar.
     * Debe ser una URL válida de Firebase Storage que contenga el path del archivo.
     * @throws ImageUrlInvalidException Si la URL proporcionada no tiene un formato válido
     * o no se puede extraer la ruta del archivo de ella.
     * @throws StorageAccessException Sí ocurre un error al intentar acceder o interactuar
     * con Firebase Storage (por ejemplo, problemas de red,
     * permisos insuficientes, configuración del bucket).
     * @throws ImageNotFoundException Si el archivo especificado por la URL no se encuentra
     * en el bucket de Firebase Storage.
     * @throws UnsupportedOperationException Si el decodificador de URL no soporta UTF-8 (altamente improbable en entornos Java modernos).
     */
    @Override
    public void deleteFile(String fileUrl) {
        try {
            String pathPrefix = "/o/";
            int startIndex = fileUrl.indexOf(pathPrefix) + pathPrefix.length();
            int endIndex = fileUrl.indexOf("?alt=media");
            if (startIndex < pathPrefix.length() || endIndex == -1) {
                throw new ImageUrlInvalidException("URL: " + fileUrl);
            }
            String encodedPath = fileUrl.substring(startIndex, endIndex);

            String filePath = URLDecoder.decode(encodedPath, StandardCharsets.UTF_8);

            Storage storage = StorageClient.getInstance().bucket().getStorage();
            BlobId blobId = BlobId.of(firebaseProperties.getStorageBucket(), filePath);
            boolean deleted = storage.delete(blobId);
            if (!deleted) {
                throw new ImageNotFoundException("Image path: " + filePath);
            }
        } catch (StorageException e) {
            throw new StorageAccessException("Bucket: " + firebaseProperties.getStorageBucket(), "URL: " + fileUrl, e.getMessage());
        }
    }

    /**
     * Obtiene la URL pública de descarga de una imagen almacenada en Firebase Storage.
     * Construye la URL basándose en el nombre de la ubicación (ruta) del archivo dentro del bucket
     * y el patrón de URL de descarga predefinido.
     *
     * @param imageUbicationName La ruta completa del archivo dentro del bucket de Firebase Storage
     * (ej., "images/mi_imagen_unica.jpg").
     * @return La URL pública de descarga de la imagen.
     * @throws ImageNotFoundException Si la imagen especificada por {@code imageUbicationName}
     * no se encuentra en el bucket de Firebase Storage.
     * @throws StorageAccessException Sí ocurre un error al intentar acceder o interactuar con Firebase Storage
     * (por ejemplo, problemas de red, permisos insuficientes, configuración del bucket incorrecta).
     * @throws UnsupportedOperationException Si el codificador de URL no soporta UTF-8 (altamente improbable
     * en entornos Java modernos, ya que StandardCharsets.UTF_8 siempre está soportado).
     */
    public String getImageUrl(String imageUbicationName) {
        try {
            Storage storage = StorageClient.getInstance().bucket().getStorage();
            BlobId blobId = BlobId.of(firebaseProperties.getStorageBucket(), imageUbicationName);
            Blob blob = storage.get(blobId);
            if (blob == null) {
                throw new ImageNotFoundException("Image path: " + imageUbicationName);
            }
            String encodedPath = URLEncoder.encode(imageUbicationName, StandardCharsets.UTF_8);
            return String.format(DOWNLOAD_URL, firebaseProperties.getStorageBucket(), encodedPath);
        } catch (StorageException e) {
            throw new StorageAccessException("Bucket: " + firebaseProperties.getStorageBucket(), "Path: " + imageUbicationName, e.getMessage());
        }
    }
}