package ru.eriknas.brokenstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ProfanityValidatorServiceConfig {

    @Value("${profanity.validator.service.url}")
    private String url;

    public ProfanityValidatorServiceConfig() {
    }

    @Bean(name = "profanityValidatorServiceClient")
    public RestClient profanityValidatorServiceClient() {
        return RestClient.builder()
                .baseUrl(url)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}