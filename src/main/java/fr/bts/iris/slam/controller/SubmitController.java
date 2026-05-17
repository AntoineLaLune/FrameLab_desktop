package fr.bts.iris.slam.controller;

import fr.bts.iris.slam.dto.SubmitResponse;
import fr.bts.iris.slam.model.Project;
import fr.bts.iris.slam.model.ViewEnum;
import fr.bts.iris.slam.service.SubmitService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static fr.bts.iris.slam.Main.navTo;

public class SubmitController extends Controller {

    @FXML private VBox submitPreview;
    @FXML private Label feedbackLabel;

    private Project project;

    private static final String CHALLENGES_DIR = "Challenges/";
    private static String userProjectsDir;

    private static String path;

    private final SubmitService submitService = new SubmitService();

    @FXML
    public void initialize() {

    }

    @Override
    public void setString(String name, String value){
        if (name.equals("userProjectsDir")) {
            userProjectsDir = value;
        }
        if (name.equals("path")) {
            path = value;
        }
    }

    @Override
    public void setProject(String name, Project value){
        if (name.equals("default")) {
            project = value;
            File file = new File(userProjectsDir + "Project " + project.getId() + "/-1.png");
            Image projectImage = new Image(file.toURI().toString());
            ImageView projectImageView = new ImageView();
            projectImageView.setImage(projectImage);
            projectImageView.setPreserveRatio(true);
            projectImageView.setFitHeight(512);
            projectImageView.setFitWidth(512);
            submitPreview.getChildren().add(projectImageView);
        }
    }

    @FXML
    private void submit() {
        if (project.getUser_id() == -1) {
            feedbackLabel.setText("L'envoie est indisponible en mode demo.");
            return;
        }

        feedbackLabel.setText("Envoie en cours...");

        Task<SubmitResponse> task = new Task<>() {
            @Override
            protected SubmitResponse call() {
                return submitService.submit(path, project.getChallenge_id());
            }
        };

        task.setOnSucceeded(event -> {
            try {
                feedbackLabel.setText(task.get().getMessage());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        task.setOnFailed(event -> feedbackLabel.setText(event.getSource().getException().getMessage()));

        new Thread(task).start();

    }

    @FXML
    private void goBack() throws IOException {
        Controller editorController = navTo(ViewEnum.EDITOR);
        editorController.setString("CHALLENGES_DIR", CHALLENGES_DIR);
        editorController.setString("userProjectsDir", userProjectsDir);
        editorController.setProject("exist", project);
    }

}
