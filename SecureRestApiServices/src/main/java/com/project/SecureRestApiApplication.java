package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SecureRestApiApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SecureRestApiApplication.class, args);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			context.close();
		}));
	}
}
