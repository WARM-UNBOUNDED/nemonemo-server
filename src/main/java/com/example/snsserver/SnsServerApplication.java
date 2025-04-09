package com.example.snsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SnsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnsServerApplication.class, args);
    }

}
