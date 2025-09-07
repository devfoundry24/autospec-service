package com.wm.common.autospec.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableMongoRepositories(basePackages = "com.wm")
@ComponentScan(basePackages = "com.wm")
@SpringBootApplication(scanBasePackages = "com.wm")
@Slf4j
public class AutospecApplication {

	public static void main(String[] args) {

		log.info("Starting Autospec Service ...");

		// Run the Spring Boot application
		ApplicationContext context = SpringApplication.run(AutospecApplication.class, args);

		log.info("Autospec Service started successfully.");
	}

}
