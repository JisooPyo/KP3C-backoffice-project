package com.example.kp3coutsourcingproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KP3COutsourcingProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(KP3COutsourcingProjectApplication.class, args);
	}

}
