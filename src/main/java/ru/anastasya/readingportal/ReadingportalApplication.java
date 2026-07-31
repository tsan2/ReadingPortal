package ru.anastasya.readingportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ReadingportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadingportalApplication.class, args);
	}

}
