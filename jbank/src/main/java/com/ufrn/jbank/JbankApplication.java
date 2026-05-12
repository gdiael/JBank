package com.ufrn.jbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ufrn.jbank.adapter.MainCli;

@SpringBootApplication
public class JbankApplication implements CommandLineRunner {

	@Autowired
	private MainCli cli;

	public static void main(String[] args) {
		SpringApplication.run(JbankApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Aplicação JBank iniciada!");

		cli.runCli();
	}

}
