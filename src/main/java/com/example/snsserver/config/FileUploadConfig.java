package com.example.snsserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUploadConfig {

    @Bean
    public String uploadDir() {
        return "/Users/kmj1110/uploads";
    }
}