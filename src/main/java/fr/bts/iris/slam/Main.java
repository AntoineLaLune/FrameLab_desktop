package fr.bts.iris.slam;

import fr.bts.iris.slam.model.ViewEnum;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewEnum.LOGIN.toString()));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void showHomeScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(ViewEnum.HOME.toString()));
        Parent root = loader.load();

        Scene newScene = new Scene(root);
        primaryStage.setTitle("Home");
        primaryStage.setScene(newScene);
    }}
