package ru.elfin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ElfinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElfinApplication.class, args);
    }

}
