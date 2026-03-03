package fr.bts.iris.slam.controller;

import fr.bts.iris.slam.dao.ProjectDAO;
import fr.bts.iris.slam.dto.ChallengeResponse;
import fr.bts.iris.slam.model.*;
import fr.bts.iris.slam.service.ChallengeService;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static fr.bts.iris.slam.Main.navTo;

public class HomeController extends Controller {

    @FXML private VBox currentChallengeVbox;
    @FXML private ImageView challengeImage;
    @FXML private Text challengeTitleText;
    @FXML private Text challengeDescriptionText;
    @FXML private Text challengeStartDateText;
    @FXML private Text challengeEndDateText;
    @FXML private Button challengeDownloadButton;
    @FXML private Label challengeFeedbackLabel;

    @FXML private VBox projectsSection;
    @FXML private TextField projectNameField;

    private static User user;
    private static Challenge challenge;
    private static ArrayList<Project> projects;
    private static ProjectDAO projectDAO;
    private static String dir;

    private final ChallengeService challengeService = new ChallengeService();

    @Override
    protected void setUser(String name, User value) { // ← Temporary send the user with a setter, for development only (Will be changed)
        if (name == "default") {
            user = value;
        }
    }

    public void initialize() {
        challengeDownloadButton.setOnAction(e -> {
            try {
                handleCreateProject();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        try {
            projectDAO = new ProjectDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadChallenge(); // Into loadProjects() ← Need to change that in the future
    }

    @FXML
    private void handleCreateProject() throws IOException {
        String name = projectNameField.getText();
        Project project = new Project(name, user.getId(), challenge.getId(), challenge.getTitle());
        projectDAO.insert(project); project.setId(projectDAO.getLastId(user.getId()));

        new File(dir+(project.getId()+"/")).mkdir();

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(challengeImage.getImage(), null);
        File challengeImage = new File(dir+(project.getId()+"/")+"0.png");
        ImageIO.write(bufferedImage, "png", challengeImage);

        buildJavaFxProjectSection(project);

        projectNameField.setText("");
    }

    @FXML
    public void loadProjects() {
        dir = "Users/" + (user.getId()+"/") + "Challenges/" + (challenge.getId()+"/") + "Projects/";
        new File(dir).mkdirs();
        projects = projectDAO.getAll(user.getId());
        for (int i = 0; i < projects.size(); i++) {
            int project_id = projects.get(i).getId();
            Path path = Path.of(dir + String.valueOf(project_id));
            if (!Files.exists(path)) {
                projectDAO.deteteById(i);
            }
        }

        for (int i = 0; i < projects.size(); i++) {
            try {
                buildJavaFxProjectSection(projects.get(i));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void buildJavaFxProjectSection(Project project) throws IOException {
        BufferedImage projectBufferedImage = ImageIO.read(new File(dir+(project.getId()+"/")+"0.png"));
        Image projectImage = SwingFXUtils.toFXImage(projectBufferedImage, null);

        VBox vBox = new VBox();
        vBox.setId("project" + project.getId() + "Section");
        projectsSection.getChildren().add(vBox);

        HBox informationHBox = new HBox();
        informationHBox.setId("project" + project.getId() + "InformationSection");
        vBox.getChildren().add(informationHBox);
        HBox interactionHBox = new HBox();
        interactionHBox.setId("project" + project.getId() + "InteractionSection");
        vBox.getChildren().add(interactionHBox);

        ImageView projectImageView = new ImageView();
        projectImageView.setImage(projectImage);
        projectImageView.setFitWidth(100.0);
        projectImageView.setPreserveRatio(true);
        informationHBox.getChildren().add(projectImageView);
        Text projectTitleTexte = new Text(project.getName());
        informationHBox.getChildren().add(projectTitleTexte);

        Button editButton = new Button("Éditer");
        interactionHBox.getChildren().add(editButton);
    }

    @FXML
    private void loadChallenge() {
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
                    challenge = body.getChallenge();
                    if (challenge != null) {
                        challengeFeedbackLabel.setVisible(false);
                        challengeImage.setImage(new Image(UrlEnum.UPLOAD.toString() + challenge.getPhoto_url()));
                        challengeTitleText.setText(challenge.getTitle());
                        challengeDescriptionText.setText(challenge.getDescription());
                        challengeStartDateText.setText(challenge.getStart_date());
                        challengeEndDateText.setText(challenge.getEnd_date());
                    } else {
                        currentChallengeVbox.setVisible(false);
                        challengeFeedbackLabel.setText("Il n'y a pas de challenge en cours à participer.");
                    }
                    loadProjects(); // Second task
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

}
