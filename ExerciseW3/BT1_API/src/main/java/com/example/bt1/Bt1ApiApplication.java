package com.example.bt1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.bt1.config.StorageProperties;
import com.example.bt1.service.IStorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Bt1ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Bt1ApiApplication.class, args);
	}

	// thêm cấu hình storage
	@Bean
	CommandLineRunner init(IStorageService storageService) {
		return (args -> {
			storageService.init();
		});
	}
}
