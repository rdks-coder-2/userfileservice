package com.extrahop.codingexercise.userfileservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.extrahop.codingexercise.userfileservice.controller.UserFileController;

@SpringBootApplication
public class UserFileServiceApplication {

	public static void main(String[] args) {
		if (args == null || args.length < 1) {
			System.err.println("--- No input file found. Exiting. ---");
			System.out.println("Usage: userfileservice <userfile>");
			System.exit(0);
		}
		SpringApplication.run(UserFileServiceApplication.class, args);
	}

	@Bean
	@Profile("!test")
	CommandLineRunner runner(UserFileController userFileController) {
		return args -> {
			userFileController.setInputFile(args[0]);
		};
	}
}
