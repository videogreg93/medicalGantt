package sample;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.sun.tools.javac.util.List;
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
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sample.EncryptionHandler.decrypt;
import static sample.EncryptionHandler.encrypt;

public class Controller {

    @FXML
    public JFXDatePicker datePicker;
    public TextField numDossier; // numéro de dossier du patient
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
        Document arrivalDate = new Document("day", datePicker.getValue().getDayOfMonth())
                .append("month", datePicker.getValue().getMonthValue())
                .append("year", datePicker.getValue().getYear());
        Document doc = new Document("Arrival Time", arrivalTime.getValue())
                .append("Time to begin Operation", operationField.getText())
                .append("Duration", DureeOperation.getText())
                .append("Operation Type", operationCombobox.getSelectionModel().getSelectedItem())
                .append("Doctor Name", doctorCombobox.getSelectionModel().getSelectedItem())
                .append("Arrival Date", arrivalDate)
                .append("Dossier", (encrypt(numDossier.getText()))); // TODO this will be encrypted
        DatabaseManager.insertNewUrgence(doc);
        showValidationMessage();
        clearFormValues();
    }

    private void clearFormValues() {
        numDossier.clear();
        operationField.clear();
        DureeOperation.clear();
        operationCombobox.getSelectionModel().clearSelection();
        doctorCombobox.getSelectionModel().clearSelection();
        //labelSpecialty;
    }


    private void showValidationMessage() {
        // Show Validation Message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Envoyé");
        alert.setHeaderText(null);
        alert.setContentText("Urgence bien envoyée!");
        alert.showAndWait();
    }



    private boolean isValidSubmission() {
        // TODO set error css
        boolean isValid = true;
        String errorMessage = "";
        // Patient Dossier number
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(numDossier.getText());
        if (!m.find()) {
            isValid = false;
            errorMessage += "Le numéro de dossier doit être un nombre\n";
        }
        // Date picker
        p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        m = p.matcher(datePicker.getValue().toString());
        if (!m.find()) {
            isValid = false;
            errorMessage += "Veuillez rentrez une date valide\n";
        }
        // arrival time
        if (arrivalTime.getEditor().getText().isEmpty()) {
            isValid = false;
            errorMessage += "Le temps d'arrivé ne peut être nul.\n";
        }
        // Temps de latence
        p = Pattern.compile("\\d{1,3}h?");
        m = p.matcher(operationField.getText());
        if (!m.find()) {
            isValid = false;
            errorMessage += "Le temps de latence n'est pas valide\n";
        }
        m = p.matcher("\\d{1,3}h?");
        if (!m.find()) {
            isValid = false;
            errorMessage += "La durée de opération n'es pas valide\n";
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
