package com.example.snsserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class SecretsManagerConfig {

    @Bean
    public String dbPassword() {
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        GetSecretValueRequest request = GetSecretValueRequest.builder()
                .secretId("snsserver/db-credentials")
                .build();

        GetSecretValueResponse response = client.getSecretValue(request);
        String secret = response.secretString();

        try {
            ObjectMapper mapper = new ObjectMapper();
            var secretMap = mapper.readValue(secret, java.util.Map.class);
            return (String) secretMap.get("password");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse secret: " + e.getMessage(), e);
        }
    }
}