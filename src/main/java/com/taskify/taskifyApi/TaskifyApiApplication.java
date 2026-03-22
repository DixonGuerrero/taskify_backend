package com.taskify.taskifyApi;

import com.taskify.taskifyApi.infrastructure.config.FirebaseProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(FirebaseProperties.class)
@SpringBootApplication
public class TaskifyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskifyApiApplication.class, args);
	}

}