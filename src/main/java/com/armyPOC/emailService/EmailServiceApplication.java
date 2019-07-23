package com.armyPOC.emailService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.core.JmsTemplate;

@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class EmailServiceApplication {

	public static void main(String[] args) {


		SpringApplication.run(EmailServiceApplication.class, args);

	}

}
