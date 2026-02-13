package fr.bts.iris.slam.controller;

import fr.bts.iris.slam.dto.ChallengeResponse;
import fr.bts.iris.slam.dto.UserResponse;
import fr.bts.iris.slam.model.Challenge;
import fr.bts.iris.slam.model.UrlEnum;
import fr.bts.iris.slam.service.ChallengeService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.concurrent.ExecutionException;

public class HomeController {

    @FXML private VBox currentChallengeVbox;
    @FXML private ImageView challengeImage;
    @FXML private Text challengeTitleText;
    @FXML private Text challengeDescriptionText;
    @FXML private Text challengeStartDateText;
    @FXML private Text challengeEndDateText;
    @FXML private Button challengeDownloadButton;
    @FXML private Label challengeFeedbackLabel;

    private final ChallengeService challengeService = new ChallengeService();

    public void initialize() {
        challengeDownloadButton.setOnAction(e -> handleChallengeDownload());
        handleChallenge();
    }

    @FXML
    private void handleChallenge() {
        challengeFeedbackLabel.setVisible(true);
        challengeFeedbackLabel.setText("Connexion en cours...");

        Task<ChallengeResponse> task = new Task<>() {
            @Override
            protected ChallengeResponse call() {
                return challengeService.getCurrent();
            }
        };

        task.setOnSucceeded(event -> {
            try {
                ChallengeResponse body = task.get();
                if (body != null) {
                    Challenge challenge = body.getChallenge();
                    if (challenge != null) {
                        challengeFeedbackLabel.setVisible(false);
                        System.out.println(UrlEnum.UPLOAD.toString() + challenge.getPhoto_url());
                        challengeImage.setImage(new Image(UrlEnum.UPLOAD.toString() + challenge.getPhoto_url()));
                        challengeTitleText.setText(challenge.getTitle());
                        challengeDescriptionText.setText(challenge.getDescription());
                        challengeStartDateText.setText(challenge.getStart_date());
                        challengeEndDateText.setText(challenge.getEnd_date());
                    } else {
                        currentChallengeVbox.setVisible(false);
                        challengeFeedbackLabel.setText("Il n'y a pas de challenge en cours à participer.");
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        task.setOnFailed(event -> {
            challengeFeedbackLabel.setText("Erreur : impossible de contacter le serveur.");
        });

        new Thread(task).start();
    }

    @FXML
    private void handleChallengeDownload() {

    }

}
