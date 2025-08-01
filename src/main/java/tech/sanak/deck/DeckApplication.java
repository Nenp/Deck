package tech.sanak.deck;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeckApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeckApplication.class, args);
	}

    @PostConstruct
    public void printEnv() {
//        System.out.println("POSTGRES_USER = " + System.getenv("POSTGRES_USER"));
//        System.out.println("POSTGRES_PASSWORD = " + System.getenv("POSTGRES_PASSWORD"));
    }
}

