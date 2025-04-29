package ru.eriknas.brokenstore;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Swagger Broken-Store - OpenAPI 3.0",
				version = "1.0.0",
				description = "В документе описано REST API интернет-магазина продажи футболок Broken-Store.\n" +
						"Дополнительная информация доступна в [репозитории магазина](https://github.com/ErikNas/broken-store)."
		)
)
@SpringBootApplication
public class BrokenStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrokenStoreApplication.class, args);
    }

}
