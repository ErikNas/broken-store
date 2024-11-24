package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.eriknas.brokenstore.services.GeneralService;

@RestController
@RequestMapping("/api/hello")
@Tag(
        name = "Hello",
        description = "Группа тестовых методов для того, чтобы научиться работать со свагером"
)
public class HelloController {

    private final GeneralService service;

    @Autowired
    public HelloController(GeneralService service) {
        this.service = service;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Поздороваться со Стасом")
    public String getHello(Model model) {
        return "Hello, Stasyan";
    }

}
