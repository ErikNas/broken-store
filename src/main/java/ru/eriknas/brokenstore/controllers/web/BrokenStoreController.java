package ru.eriknas.brokenstore.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.eriknas.brokenstore.services.GeneralService;

@Controller
public class BrokenStoreController {

    private final GeneralService service;

    public BrokenStoreController(GeneralService service) {
        this.service = service;
    }

    @GetMapping
    public String indexPage(Model model) {
        return "index";
    }
}
