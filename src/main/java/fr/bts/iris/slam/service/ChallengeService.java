package fr.bts.iris.slam.service;

import fr.bts.iris.slam.exceptions.ChallengeServiceException;
import tools.jackson.databind.ObjectMapper;
import fr.bts.iris.slam.dto.ChallengeResponse;
import fr.bts.iris.slam.model.UrlEnum;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChallengeService {

    private final HttpClient client;
    private final ObjectMapper mapper;

    public ChallengeService() {
        this.client = ClientManager.getHttpClient();
        this.mapper = new ObjectMapper();
    }

    public ChallengeResponse getCurrent() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UrlEnum.CHALLENGE.toString()))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            ChallengeResponse jsonChallengeResponse = mapper.readValue(response.body(), ChallengeResponse.class);

            if (response.statusCode() > 0) {
                return jsonChallengeResponse;
            }

            throw new ChallengeServiceException("Il n'y a pas de challenge en cours à participer.");

        } catch (IOException | InterruptedException e) {
            throw new ChallengeServiceException("Impossible de contacter le serveur", e);
        }
    }

}
