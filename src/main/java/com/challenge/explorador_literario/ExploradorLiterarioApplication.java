package com.challenge.explorador_literario;

import com.challenge.explorador_literario.api.ApiClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExploradorLiterarioApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ExploradorLiterarioApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ApiClient apiClient = new ApiClient();
		System.out.println(apiClient.getData("https://gutendex.com/books/"));
	}
}
