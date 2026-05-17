package fr.bts.iris.slam.controller;

import fr.bts.iris.slam.dao.ProjectDAO;
import fr.bts.iris.slam.dto.ChallengeResponse;
import fr.bts.iris.slam.model.*;
import fr.bts.iris.slam.service.ChallengeService;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
import static fr.bts.iris.slam.service.ClientManager.*;

public class HomeController extends Controller {

    @FXML private Text userName;
    @FXML private VBox currentChallengeVbox;
    @FXML private ImageView challengeImage;
    @FXML private Text challengeTitleText;
    @FXML private Text challengeDescriptionText;
    @FXML private Text challengeStartDateText;
    @FXML private Text challengeEndDateText;
    @FXML private TextField challengeTextField;
    @FXML private Button challengeDownloadButton;
    @FXML private Label challengeFeedbackLabel;

    @FXML private VBox projectsSection;
    @FXML private TextField projectNameField;

    private static Challenge challenge;
    private static ProjectDAO projectDAO;
    private static final String CHALLENGES_DIR = "Challenges/";
    private static String userProjectsDir;

    private final ChallengeService challengeService = new ChallengeService();

    public void initialize() {
        try {
            projectDAO = new ProjectDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        userName.setText(getCurrentUser().getFirst_name()+getCurrentUser().getId());
        userProjectsDir = "Users/" + (getCurrentUser().getId()+"/");
        loadChallenge();
        loadProjects();

        challengeImage.isFocused();
        challengeDownloadButton.setOnAction(e -> {
            try {
                handleDownloadChallengeImage();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void loadChallenge() {

        if (getCurrentUser().getId() == -1) {
            challenge = new Challenge();
            challenge.setId(-1);
            challenge.setTitle("Demo");

            File file = new File("src/main/resources/fox.png");
            Image image = new Image(file.toURI().toString());
            challengeImage.setImage(image);
            challengeTitleText.setText("Demo");
            challengeDescriptionText.setText("Demo");
            challengeStartDateText.setText("Début : demo");
            challengeEndDateText.setText("Fin : demo");
        } else {
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
                            challengeImage.setImage(new Image(UrlEnum.UPLOAD + challenge.getPhoto_url()));
                            challengeTitleText.setText(challenge.getTitle());
                            challengeDescriptionText.setText(challenge.getDescription());
                            challengeStartDateText.setText("Début : " + challenge.getStart_date());
                            challengeEndDateText.setText("Fin : " + challenge.getEnd_date());
                        } else {
                            currentChallengeVbox.setVisible(false);
                            challengeFeedbackLabel.setText("Il n'y a pas de challenge en cours à participer.");
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });

            task.setOnFailed(event -> challengeFeedbackLabel.setText("Erreur : impossible de contacter le serveur."));

            new Thread(task).start();
        }
    }

    public void loadProjects() {

        new File(userProjectsDir).mkdirs();
        ArrayList<Project> projects = projectDAO.getAll(getCurrentUser().getId());
        for (int i = projects.size() - 1; i >= 0; i--) {
            int project_id = projects.get(i).getId();
            Path path = Path.of(userProjectsDir + "Project " + project_id);

            if (!Files.exists(path)) {
                projectDAO.delete(project_id);
            }
        }

        projects = projectDAO.getAll(getCurrentUser().getId());
        for (Project project : projects) {
            buildJavaFxProjectSection(project);
        }

    }

    @FXML
    private void handleCreateProject() throws IOException {
        String name = projectNameField.getText();
        Project project = new Project(name, getCurrentUser().getId(), challenge.getId(), challenge.getTitle());
        projectDAO.insert(project); project.setId(projectDAO.getLastId(getCurrentUser().getId()));

        new File(CHALLENGES_DIR).mkdir();
        new File(userProjectsDir+"Project "+project.getId()+"/").mkdir();

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(challengeImage.getImage(), null);
        File challengeImage = new File(CHALLENGES_DIR+project.getChallenge_id()+".png");
        ImageIO.write(bufferedImage, "png", challengeImage);

        projectNameField.setText("");
        Controller editorController = navTo(ViewEnum.EDITOR);
        editorController.setString("CHALLENGES_DIR", CHALLENGES_DIR);
        editorController.setString("userProjectsDir", userProjectsDir);
        editorController.setProject("new", project);
    }

    private static Button getEditButton(Project project) {
        Button editButton = new Button("Éditer");
        editButton.setId(Integer.toString(project.getId()));
        editButton.setOnAction(event -> {
            try {
                Controller editorController = navTo(ViewEnum.EDITOR);
                editorController.setString("CHALLENGES_DIR", CHALLENGES_DIR);
                editorController.setString("userProjectsDir", userProjectsDir);
                editorController.setProject("exist", projectDAO.get(getCurrentUser().getId(), Integer.parseInt(editButton.getId())));
            } catch (IOException e) { throw new RuntimeException(e); }
        });
        return editButton;
    }

    @FXML
    private void handleDownloadChallengeImage() throws IOException {

        Path path = Path.of(System.getProperty("user.home"), challengeTextField.getText() + ".png");

        File challengeFile = new File(CHALLENGES_DIR + challenge.getId() + ".png");
        File homeFile = new File(System.getProperty("user.home"), challengeTextField.getText() + ".png");

        if (!Files.exists(path)) {
            Image challengeImage = new Image(challengeFile.toURI().toString());
            BufferedImage bufferedChallengeImage = SwingFXUtils.fromFXImage(challengeImage, null);

            ImageIO.write(bufferedChallengeImage, "png", homeFile);

            challengeFeedbackLabel.setVisible(true);
            challengeFeedbackLabel.setText("Succès, retrouvez votre image sous " + homeFile.getAbsolutePath());
        } else {
            challengeFeedbackLabel.setVisible(true);
            challengeFeedbackLabel.setText("Echec, une image existe déjà sous " + homeFile.getAbsolutePath());
        }

    }

    @FXML
    private void logout() throws IOException {
        setCurrentUser(new User());
        navTo(ViewEnum.LOGIN);
    }

    // JavaFX build functions ↓
    public void buildJavaFxProjectSection(Project project) {
        File file = new File(userProjectsDir + "Project " + project.getId() + "/-1.png");
        Image projectImage = new Image(file.toURI().toString());

        VBox vBox = new VBox();
        vBox.setId("project" + project.getId() + "Section");
        projectsSection.getChildren().add(vBox);

            HBox informationHBox = new HBox();
            informationHBox.setId("project" + project.getId() + "InformationSection");
            Insets insets = new Insets(4.0);
            informationHBox.setPadding(insets);
        vBox.getChildren().add(informationHBox);

            HBox interactionHBox = new HBox();
            interactionHBox.setId("project" + project.getId() + "InteractionSection");
            interactionHBox.setPadding(insets);
        vBox.getChildren().add(interactionHBox);

            ImageView projectImageView = new ImageView();
            projectImageView.setImage(projectImage);
            projectImageView.setFitWidth(100.0);
            projectImageView.setPreserveRatio(true);
        informationHBox.getChildren().add(projectImageView);
            Text projectTitleText = new Text(" "+project.getName());
        informationHBox.getChildren().add(projectTitleText);

            Button editButton = getEditButton(project);
        interactionHBox.getChildren().add(editButton);
    }

}
