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
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public ChallengeResponse getCurrent() {
        try {

            System.out.println("execution");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UrlEnum.CHALLENGE.toString()))
                    .GET()
                    .build();

            System.out.println("entre 2");

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println("execution second");

            ChallengeResponse jsonChallengeResponse = mapper.readValue(response.body(), ChallengeResponse.class);

            System.out.println("jsonChallengeResponse " + response.body());

            if (response.statusCode() > 0) {
                return jsonChallengeResponse;
            }

            throw new ChallengeServiceException("Il n'y a pas de challenge en cours à participer.");

        } catch (IOException | InterruptedException e) {
            throw new ChallengeServiceException("Impossible de contacter le serveur", e);
        }
    }

    public void download() {

    }

}
