package com.taskify.taskifyApi.infrastructure.output.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.taskify.taskifyApi.infrastructure.config.FirebaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Configuration
public class FirebaseConfig {

    private final FirebaseProperties firebaseProperties;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = getClass()
                    .getClassLoader()
                    .getResourceAsStream("firebase/clave_privada.json");

            if (serviceAccount == null) {
                System.out.println("⚠️ Firebase no configurado: archivo no encontrado, se omite inicialización.");
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(firebaseProperties.getStorageBucket())
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("✅ Firebase inicializado correctamente");

        } catch (Exception e) {
            System.out.println("⚠️ Error inicializando Firebase, se ignora: " + e.getMessage());
        }
    }
}