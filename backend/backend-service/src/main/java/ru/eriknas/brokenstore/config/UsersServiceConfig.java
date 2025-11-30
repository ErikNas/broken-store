package ru.eriknas.brokenstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class UsersServiceConfig {

    @Value("${user.service.url}")
    private String url;

    @Value("${user.service.login}")
    private String login;

    @Value("${user.service.password}")
    private String password;

    public UsersServiceConfig() {
    }

    @Bean
    public RestClient usersServiceClient() {
        return RestClient.builder()
                .baseUrl(url)
                .defaultHeader("Accept", "application/json")
                .build();
    }
}