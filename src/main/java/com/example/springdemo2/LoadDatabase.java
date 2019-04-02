package com.example.springdemo2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Slf4j
@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDataBase(UserRepository repository){
        return args -> {
            log.info("saved to database: " + repository.save(new User("Flo Rider", BigDecimal.valueOf(12))));
            log.info("saved to database: "+ repository.save(new User("Joe Landa", BigDecimal.valueOf(42))));
        };
    }
}
