package com.ufrn.jbank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JbankApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JbankApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Aplicação JBank iniciada!");
	}

}
