package fr.bts.iris.slam.controller;

import fr.bts.iris.slam.dto.LoginResponse;
import fr.bts.iris.slam.model.User;
import fr.bts.iris.slam.model.ViewEnum;
import fr.bts.iris.slam.service.LoginService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static fr.bts.iris.slam.Main.navTo;
import static fr.bts.iris.slam.service.ClientManager.setCurrentUser;

public class LoginController extends Controller {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label feedbackLabel;

    private final LoginService loginService = new LoginService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        feedbackLabel.setText("Connexion en cours...");

        Task<LoginResponse> task = new Task<>() {
            @Override
            protected LoginResponse call() {
                return loginService.login(email, password);
            }
        };

        task.setOnSucceeded(event -> {
            try {
                User user = task.get().getData();
                if (user != null) {
                    setCurrentUser(user);
                    feedbackLabel.setText("Bienvenue, " + user.getEmail() + " !");
                    navTo(ViewEnum.HOME);
                } else {
                    feedbackLabel.setText(task.get().getMessage());
                }
            } catch (InterruptedException | ExecutionException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        task.setOnFailed(event -> feedbackLabel.setText("Erreur : impossible de contacter le serveur."));

        new Thread(task).start();

    }

    @FXML
    private void handleDemo() throws IOException {
        User user = new User();
        user.setId(-1);
        user.setFirst_name("Demo");
        user.setLast_name("Demo");
        user.setEmail("Demo");

        setCurrentUser(user);

        feedbackLabel.setText("Bienvenue, " + user.getEmail() + " !");
        navTo(ViewEnum.HOME);
    }

}
