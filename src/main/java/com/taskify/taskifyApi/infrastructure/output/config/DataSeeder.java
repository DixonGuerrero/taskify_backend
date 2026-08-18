package com.taskify.taskifyApi.infrastructure.output.config;

import com.taskify.taskifyApi.application.ports.output.FilePersistencePort;
import com.taskify.taskifyApi.application.ports.output.ImagePersistencePort;
import com.taskify.taskifyApi.application.ports.output.UserPersistencePort;
import com.taskify.taskifyApi.domain.enums.ImageType;
import com.taskify.taskifyApi.domain.enums.RoleEnum;
import com.taskify.taskifyApi.domain.model.File;
import com.taskify.taskifyApi.domain.model.Image;
import com.taskify.taskifyApi.domain.model.Role;
import com.taskify.taskifyApi.domain.model.User;
import com.taskify.taskifyApi.infrastructure.output.entity.RoleEntity;
import com.taskify.taskifyApi.infrastructure.output.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final String SEED_ADMIN_USERNAME = "admin";
    private static final String SEED_ADMIN_EMAIL = "admin@taskify.dev";
    private static final String SEED_ADMIN_PASSWORD = "Admin123!";

    private final RoleRepository roleRepository;
    private final UserPersistencePort userPersistencePort;
    private final ImagePersistencePort imagePersistencePort;
    private final FilePersistencePort filePersistencePort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!roleRepository.findAll().isEmpty()) {
            log.info("🌱 Seed omitido: ya existen roles en la base de datos.");
            return;
        }

        log.info("🌱 Base de datos vacía, sembrando datos mínimos de arranque...");

        Role adminRole = saveRole(RoleEnum.ADMIN);
        saveRole(RoleEnum.USER);

        User adminUser = userPersistencePort.save(
                User.builder()
                        .firstName("Admin")
                        .lastName("Taskify")
                        .email(SEED_ADMIN_EMAIL)
                        .username(SEED_ADMIN_USERNAME)
                        .password(passwordEncoder.encode(SEED_ADMIN_PASSWORD))
                        .isEnabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .role(adminRole)
                        .build()
        );

        Image userImage = saveDefaultImage(adminUser.getId(), ImageType.USER,
                "seed/default-user-avatar.png");
        saveDefaultImage(adminUser.getId(), ImageType.PROJECT,
                "seed/default-project-image.png");

        adminUser.setImage(userImage);
        userPersistencePort.save(adminUser);

        log.info("✅ Seed completo. Usuario admin de desarrollo -> username: '{}', password: '{}'",
                SEED_ADMIN_USERNAME, SEED_ADMIN_PASSWORD);
    }

    private Role saveRole(RoleEnum name) {
        RoleEntity saved = roleRepository.save(new RoleEntity(null, name));
        return Role.builder().id(saved.getId()).name(saved.getName()).build();
    }

    private Image saveDefaultImage(Long ownerId, ImageType type, String storageKey) {
        File file = filePersistencePort.save(
                File.builder()
                        .originalName(storageKey.substring(storageKey.lastIndexOf('/') + 1))
                        .storageKey(storageKey)
                        .extension(".png")
                        .fileSize(0L)
                        .ownerId(ownerId)
                        .build()
        );

        return imagePersistencePort.save(
                Image.builder()
                        .file(file)
                        .type(type)
                        .build()
        );
    }
}
