package fr.bts.iris.slam.service;

import tools.jackson.databind.ObjectMapper;
import fr.bts.iris.slam.dto.LoginRequest;
import fr.bts.iris.slam.dto.UserResponse;
import fr.bts.iris.slam.exceptions.LoginServiceException;
import fr.bts.iris.slam.model.UrlEnum;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginService {

    private final HttpClient client;
    private final ObjectMapper mapper;

    public LoginService() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.client = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .build();
        this.mapper = new ObjectMapper();
    }

    public UserResponse login(String email, String password) {
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

            UserResponse jsonUserResponse = mapper.readValue(response.body(), UserResponse.class);

            if (response.statusCode() > 0) {
                return jsonUserResponse;
            }

            throw new LoginServiceException(jsonUserResponse.getMessage());

        } catch (IOException | InterruptedException e) {
            throw new LoginServiceException("Impossible de contacter le serveur", e);
        }
    }

}