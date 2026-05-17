package fr.bts.iris.slam.service;

import com.github.mizosoft.methanol.MediaType;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import com.github.mizosoft.methanol.MutableRequest;
import fr.bts.iris.slam.dto.SubmitResponse;

import fr.bts.iris.slam.exceptions.SubmitServiceException;
import fr.bts.iris.slam.model.UrlEnum;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class SubmitService {

    private final HttpClient client;
    private final ObjectMapper mapper;

    public SubmitService() {
        this.client = ClientManager.getHttpClient();
        this.mapper = new ObjectMapper();
    }

    public SubmitResponse submit(String path, int challengeId) {
        try {
            MultipartBodyPublisher multipartBody = MultipartBodyPublisher.newBuilder()
                    .filePart("file", Path.of(path), MediaType.IMAGE_ANY)
                    .textPart("challenge_id", String.valueOf(challengeId))
                    .build();

            HttpRequest request = MutableRequest.newBuilder()
                    .uri(URI.create(UrlEnum.SUBMIT.toString()))
                    .POST(multipartBody)
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            SubmitResponse jsonSubmitResponse = mapper.readValue(response.body(), SubmitResponse.class);

            if (response.statusCode() > 0) {
                return jsonSubmitResponse;
            }

            throw new SubmitServiceException(jsonSubmitResponse.getMessage());

        } catch (IOException | InterruptedException e) {
            throw new SubmitServiceException("Impossible de contacter le serveur", e);
        }
    }

}
