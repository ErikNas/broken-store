package ru.eriknas.brokenstore.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceComponent {
    private final RestClient usersServiceClient;

    /**
     * Проверяет существование пользователя по ID.
     *
     * @param userId ID пользователя
     * @return ResponseEntity с статусом ответа
     * @throws RestClientResponseException если пользователь не найден
     */
    public ResponseEntity<Void> getUser(int userId) throws RestClientResponseException {
        log.debug("Checking user existence: userId={}", userId);

        ResponseEntity<Void> response = usersServiceClient.get()
                .uri("/user-service/users/{id}", userId)
                .retrieve()
                .toBodilessEntity();

        log.debug("User service response: status={}, userId={}", response.getStatusCode(), userId);
        return response;
    }
}
