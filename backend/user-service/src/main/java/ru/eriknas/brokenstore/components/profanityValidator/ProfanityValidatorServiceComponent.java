package ru.eriknas.brokenstore.components.profanityValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import ru.eriknas.brokenstore.components.profanityValidator.dto.ProfanityValidateRequest;
import ru.eriknas.brokenstore.components.profanityValidator.dto.ProfanityValidateResponse;
import ru.eriknas.brokenstore.exception.ProfanityException;

@Service
public class ProfanityValidatorServiceComponent {
    private final RestClient profanityValidatorServiceClient;

    @Autowired
    public ProfanityValidatorServiceComponent(@Qualifier("profanityValidatorServiceClient") RestClient client) {
        this.profanityValidatorServiceClient = client;
    }

    public void validateProfanity(String textToValidate) {
        ProfanityValidateResponse resp = postValidateProfanity(textToValidate);
        if (resp.getPredict() == 1) {
            throw new ProfanityException(String.format("Фраза <%s> содержит нецензурное выражение", textToValidate));
        }
    }

    public ProfanityValidateResponse postValidateProfanity(String textToValidate) throws RestClientResponseException {
        ProfanityValidateRequest request = new ProfanityValidateRequest(textToValidate);
        return profanityValidatorServiceClient.post()
                .uri("/validate-profanity")
                .body(request)
                .retrieve()
                .body(ProfanityValidateResponse.class);
    }
}
