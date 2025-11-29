package ru.eriknas.brokenstore;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Swagger Broken-Store - OpenAPI 3.0",
				version = "1.0.0",
				description = "В документе описано REST API интернет-магазина продажи футболок Broken-Store."
		),
		externalDocs = @ExternalDocumentation(
				description = "Условия использования Swagger Broken-store",
				url = "https://github.com/ErikNas/broken-store?tab=readme-ov-file#отказ-от-ответственности"
		)
)
@SpringBootApplication
public class BrokenStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrokenStoreApplication.class, args);
	}
}