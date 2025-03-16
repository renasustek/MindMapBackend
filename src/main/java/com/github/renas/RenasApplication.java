package com.github.renas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RenasApplication {

	public static void main(String[] args) {
		SpringApplication.run(RenasApplication.class, args);
	}

}
