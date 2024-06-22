package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
    private Stage primaryStage;
   
    @Override
    public void start(Stage stage) {
        try {
            showHomePage(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit(Stage stage) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("You're about to exit");
        alert.setContentText("Are you sure you want to exit?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            stage.close();
        }
    }

    public void showHomePage(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        AnchorPane loginLayout = loader.load();

        HomeController homePage = loader.getController();
        homePage.initializeAds();

        primaryStage = new Stage();
        primaryStage.setTitle("The Posher");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(loginLayout));

        // Set the close request handler before showing the stage
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            exit(primaryStage);
        });

        primaryStage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}