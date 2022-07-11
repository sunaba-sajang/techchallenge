package com.example.techchallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Employee("John", 3000.0f)));
            log.info("Preloading " + repository.save(new Employee("John 2", 3500.0f)));
            log.info("Preloading " + repository.save(new Employee("Albert", 2500.0f)));
            log.info("Preloading " + repository.save(new Employee("Victor", 4500.0f)));
        };
    }
}
