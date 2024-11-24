package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class ServerErrorController {
    private int statusCode;

    @Operation(summary = "Ручка, которая присылает заданый статус-код")
    public int sendRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            statusCode = connection.getResponseCode();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusCode;
    }

    @GetMapping("/api/error500")
    @Operation(summary = "Ручка, которая отвечает код 500 в 100% случаев")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String alwaysReturnServerError() {
        return "Статус кода: 500";
    }

    @GetMapping("/api/flakyEndpoint")
    @Operation(summary = "Ручка, которая отвечает код 500 в 30% случаем (флакер)")
    public void flakyEndpoint() {
        if (ThreadLocalRandom.current().nextInt(100) < 30) {
            throw new RuntimeException("Internal Server Error");
        }
    }

    @GetMapping("/api/response200WithError")
    @Operation(summary = "Ручка которая присылает 200, но в теле сообщения 'Error: Я не смогла'")
    public ResponseEntity<String> response200WithError() {
        return ResponseEntity.ok("Error: Я не смогла");
    }
}
