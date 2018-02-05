package sample;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.bson.Document;

import java.util.Arrays;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        // Setup comboboxes
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox = (ComboBox<String>) root.lookup("#operationCombobox");
        comboBox.getItems().addAll("Fracture","Appendicite","Autre");

        comboBox = (ComboBox<String>) root.lookup("#doctorCombobox");
        //comboBox.getItems().addAll("Dr. Katherine Cabrejo-Jones","Dr. Charles Desautels","Dr. Jacques Desnoyers");
        // Setup Stage
        primaryStage.setTitle("Pilote");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
        // Setup mongodb
        DatabaseManager.init();
        // Load doctors into comboBox
        DatabaseManager.loadDoctorNames(comboBox);
        // Complete combox setup
        comboBox.valueProperty().addListener((ov, t, t1) -> {
            Label specialtyLabel = (Label) root.lookup("#labelSpecialty");
            specialtyLabel.setText(Controller.doctors.get(ov.getValue()));
        });




    }


    public static void main(String[] args) {
        launch(args);
    }
}
