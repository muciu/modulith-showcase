package com.muciociosan.theproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TheprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheprojectApplication.class, args);
	}

}
