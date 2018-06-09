package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setup mongodb
        DatabaseManager.init();
        //Setup View
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        // Setup Stage
        primaryStage.setTitle("Pilote");
        Scene scene = new Scene(root, 1000, 900);
        scene.getStylesheets().add("cell.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
