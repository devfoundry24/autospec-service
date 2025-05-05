package com.wm.autospec_app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.wm.autospec.data.processing.repository")
@ComponentScan(basePackages = "com.wm")
@SpringBootApplication(scanBasePackages = "com.wm")
@Slf4j
public class AutospecApplication {

	public static void main(String[] args) {

		log.info("Starting Autospec Service ...");

		// Run the Spring Boot application
		SpringApplication.run(AutospecApplication.class, args);

		log.info("Autospec Service started successfully.");
	}

}
