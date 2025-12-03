package ru.eriknas.brokenstore.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class UsersServiceComponent {
    private final RestClient usersServiceClient;

    @Autowired
    public UsersServiceComponent(@Qualifier("usersServiceClient") RestClient client) {
        this.usersServiceClient = client;
    }

    public ResponseEntity<Void> getUser(int userId) throws RestClientResponseException {
        return usersServiceClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .toBodilessEntity();
    }
}
