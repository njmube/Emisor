package com.cubetech.facturador.emisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@ComponentScan
public class EmisorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmisorApplication.class, args);
	}
}
