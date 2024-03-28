package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@RequiredArgsConstructor
@ComponentScan("com.example")
public class DemoApplication  {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("start");
	}
}
