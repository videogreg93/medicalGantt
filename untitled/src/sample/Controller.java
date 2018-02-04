package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;

public class Controller {

    @FXML
    private TextField arrivalTime; // Heure d'arrivé
    @FXML
    private TextField operationField; // Temps pour commencer l'opération
    @FXML
    public TextField DureeOperation; // Durée d'opération
    @FXML
    private ComboBox<String> operationCombobox; // Type d'opération
    @FXML
    private ComboBox<String> doctorCombobox; // Nom du médecin

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        System.out.println("Heure d'arrivé: " + arrivalTime.getText());
        System.out.println("Temps pour commencer l'opération: " + operationField.getText());
        System.out.println("Durée d'opération: " + operationField.getText());
        System.out.println("Operation: " + operationCombobox.getSelectionModel().getSelectedItem());
        System.out.println("Médecin: " + doctorCombobox.getSelectionModel().getSelectedItem());

        Document doc = new Document("Arrival Time", arrivalTime.getText())
                .append("Time to begin Operation", operationField.getText())
                .append("Duration", DureeOperation.getText())
                .append("Operation Type", operationCombobox.getSelectionModel().getSelectedItem())
                .append("Doctor Name", doctorCombobox.getSelectionModel().getSelectedItem());
        DatabaseManager.insertNewUrgence(doc);
    }
}
