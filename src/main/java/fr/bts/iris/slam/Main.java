package fr.bts.iris.slam;

import fr.bts.iris.slam.controller.Controller;
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
        ViewEnum view = ViewEnum.LOGIN;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(view.toString()));
        Scene scene = new Scene(loader.load());
        stage.setTitle(view.toString());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Controller navTo(ViewEnum view) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(view.toString()));
        Parent root = loader.load();

        Scene newScene = new Scene(root);
        primaryStage.setTitle(view.toString());
        primaryStage.setScene(newScene);
        return loader.getController();
    }

}
