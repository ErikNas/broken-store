package ru.eriknas.brokenstore.controllers.web;

import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @GetMapping("/css/styles.css")
    @ResponseBody
    public Resource getCss() {
        return new ClassPathResource("static/css/styles.css");
    }
}


