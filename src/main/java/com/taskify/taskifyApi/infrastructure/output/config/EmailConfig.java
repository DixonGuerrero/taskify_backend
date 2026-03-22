package com.taskify.taskifyApi.infrastructure.output.config;

import com.taskify.taskifyApi.infrastructure.config.EmailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author Dixon Guerrero
 * @version 1.0.0
 * @since 2025-07-13
 *
 * Clase de configuración para la integración de envío de correos electrónicos en la aplicación.
 * Utiliza Spring para la gestión de dependencias y propiedades.
 */
@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    /**
     * Inyección de las propiedades relacionadas con el correo electrónico.
     * Estas propiedades suelen ser cargadas desde un archivo de configuración (ej. application.properties/yaml).
     */
    private final EmailProperties emailProperties;

    /**
     * Configura y retorna una instancia de {@link org.springframework.mail.javamail.JavaMailSender}.
     * Este bean es esencial para el envío de correos electrónicos a través de SMTP.
     *
     * @return Una instancia configurada de {@link JavaMailSender}.
     * @Bean Indica que este método produce un bean que será gestionado por
     * el contenedor de Spring.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(emailProperties.getProperties());
        mailSender.setUsername(emailProperties.getUsername());
        mailSender.setPassword(emailProperties.getPassword());
        return mailSender;
    }

    /**
     * Configura y retorna una instancia de {@link org.springframework.core.io.ResourceLoader}.
     * Este bean es útil para cargar recursos (ej. plantillas de correo electrónico) desde diferentes ubicaciones.
     *
     * @return Una instancia por defecto de {@link ResourceLoader}.
     * @Bean Indica que este método produce un bean que será gestionado por el contenedor de Spring.
     */
    @Bean
    public ResourceLoader resourceLoader() {
        return new DefaultResourceLoader();
    }
}
