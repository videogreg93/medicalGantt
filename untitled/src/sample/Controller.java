package sample;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.text.Text;
import org.bson.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Controller {

    @FXML
    public JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker arrivalTime; // Heure d'arrivé
    @FXML
    private TextField operationField; // Temps pour commencer l'opération
    @FXML
    public TextField DureeOperation; // Durée d'opération
    @FXML
    private ComboBox<String> operationCombobox; // Type d'opération
    @FXML
    private ComboBox<String> doctorCombobox; // Nom du médecin
    @FXML
    public Label labelSpecialty; // Spécialité du médecin

    public static HashMap<String,String> doctors = new HashMap<>();


    @FXML
    public void initialize() {
        // Setup DatePicker
        datePicker.setValue(LocalDate.now());
        // Setup comboboxes
        operationCombobox.getItems().addAll("Fracture","Appendicite","Autre");
        // Load doctor names
        DatabaseManager.loadDoctorNames(doctorCombobox);
        // Complete combox setup
        doctorCombobox.valueProperty().addListener((ov, t, t1) -> {
            labelSpecialty.setText(Controller.doctors.get(ov.getValue()));
        });
    }


    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        // Validate combobox
        if (!isValidSubmission()) {
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        Document doc = new Document("Arrival Time", arrivalTime.getValue())
                .append("Time to begin Operation", operationField.getText())
                .append("Duration", DureeOperation.getText())
                .append("Operation Type", operationCombobox.getSelectionModel().getSelectedItem())
                .append("Doctor Name", doctorCombobox.getSelectionModel().getSelectedItem())
                .append("Arrival Date", datePicker.getValue().format(formatter));
        DatabaseManager.insertNewUrgence(doc);
    }

    private boolean isValidSubmission() {
        // TODO set error css
        boolean isValid = true;
        String errorMessage = "";
        // arrival time
        if (arrivalTime.getEditor().getText().isEmpty()) {
            isValid = false;
            errorMessage += "Le temps d'arrivé ne peut être nul.\n";
        }
        if (operationField.getText().isEmpty()) {
            isValid = false;
            errorMessage += "Le temps pour opérer ne peut être vide.\n";
        }
        if (DureeOperation.getText().isEmpty()) {
            isValid = false;
            errorMessage += "La durée de opération ne peut être vide.\n";
        }
        if (operationCombobox.getSelectionModel().isEmpty()) {
            isValid = false;
            errorMessage += "Veuillez choisir un type d'opération.\n";
        }
        if (doctorCombobox.getSelectionModel().isEmpty()) {
            isValid = false;
            errorMessage += "Veuillez choisir un médecin.\n";
        }
        if (!isValid) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }
        return isValid;
    }

}
