package fr.bts.iris.slam.service;

import tools.jackson.databind.ObjectMapper;
import fr.bts.iris.slam.dto.LoginRequest;
import fr.bts.iris.slam.dto.LoginResponse;
import fr.bts.iris.slam.exceptions.LoginServiceException;
import fr.bts.iris.slam.model.UrlEnum;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginService {

    private final HttpClient client;
    private final ObjectMapper mapper;

    public LoginService() {
        this.client = ClientManager.getHttpClient();
        this.mapper = new ObjectMapper();
    }

    public LoginResponse login(String email, String password) {
        try {

            String jsonBody = mapper.writeValueAsString(
                    new LoginRequest(email, password)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UrlEnum.LOGIN.toString()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            LoginResponse loginResponse = mapper.readValue(response.body(), LoginResponse.class);

            if (response.statusCode() > 0) {
                return loginResponse;
            }

            throw new LoginServiceException(loginResponse.getMessage());

        } catch (IOException | InterruptedException e) {
            throw new LoginServiceException("Impossible de contacter le serveur", e);
        }
    }

}