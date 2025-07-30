package ru.eriknas.brokenstore;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Swagger Broken-Store-User-Service - OpenAPI 3.0",
				version = "1.0.0",
				description = "В документе описано REST API сервиса user-service"
		)
)
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
