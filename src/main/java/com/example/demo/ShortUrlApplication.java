package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@SpringBootApplication
public class ShortUrlApplication {

	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor(); // Lightweight, uses virtual threads if enabled
	}

	public static void main(String[] args) {
		SpringApplication.run(ShortUrlApplication.class, args);
	}

}
