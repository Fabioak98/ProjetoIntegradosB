package com.example.HealthCare;

import com.twilio.Twilio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableAsync
public class HealthCareApplication {

	public static final String ACCOUNT_SID = "AC2e6c990758d4b1e5228c43b2dd5ece71";
	public static final String AUTH_TOKEN = "12ca8c5a199af8f02d5db3b28bff4f4f";

	public static void main(String[] args) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		SpringApplication.run(HealthCareApplication.class, args);
	}

}
