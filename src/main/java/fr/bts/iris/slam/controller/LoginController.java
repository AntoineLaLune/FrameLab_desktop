package fr.bts.iris.slam.controller;

import fr.bts.iris.slam.Main;
import fr.bts.iris.slam.dto.UserResponse;
import fr.bts.iris.slam.model.User;
import fr.bts.iris.slam.service.LoginService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label feedbackLabel;

    private final LoginService loginService = new LoginService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        feedbackLabel.setText("Connexion en cours...");

        Task<UserResponse> task = new Task<>() {
            @Override
            protected UserResponse call() {
                return loginService.login(email, password);
            }
        };

        task.setOnSucceeded(event -> {
            try {
                User user = task.get().getUserData();
                if (user != null) {
                    feedbackLabel.setText("Bienvenue, " + user.getEmail() + " !");
                    Main.showHomeScreen();
                } else {
                    feedbackLabel.setText(task.get().getMessage());
                }
            } catch (InterruptedException | ExecutionException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        task.setOnFailed(event -> {
            feedbackLabel.setText("Erreur : impossible de contacter le serveur.");
        });

        new Thread(task).start();
    }

}
