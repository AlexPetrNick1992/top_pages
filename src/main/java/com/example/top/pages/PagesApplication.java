package com.example.top.pages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class PagesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagesApplication.class, args);
	}

}
