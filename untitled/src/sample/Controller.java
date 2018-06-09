package sample;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.bson.Document;
import org.controlsfx.control.Notifications;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sample.EncryptionHandler.encrypt;

public class Controller {

    @FXML
    public JFXDatePicker datePicker;
    public TextField numDossier; // numéro de dossier du patient
    public GanttChart ganttChart;
    public Label detailsDossier;
    public Label detailsMedecin;
    public Label detailsChirurgie;
    public Label detailsHeure;
    public Label detailsDate;
    public Label detailsTempsDeLatence;
    public Label detailsDuree;
    public Button detailsDeleteButton;
    public TextField autreTextField;
    public Label     labelAutre;
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
        operationCombobox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    autreTextField.setDisable(!newValue.equalsIgnoreCase("Autre"));
                    labelAutre.setDisable(!newValue.equalsIgnoreCase("Autre"));
            }
        });

        // setup ganttChart
        ganttChart.setController(this);



    }


    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        // Validate combobox
        if (!isValidSubmission()) {
            return;
        }
        // Get operation type
        String operationType = operationCombobox.getSelectionModel().getSelectedItem();
        if (!autreTextField.isDisabled())
            operationType = autreTextField.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        Document arrivalDate = new Document("day", datePicker.getValue().getDayOfMonth())
                .append("month", datePicker.getValue().getMonthValue())
                .append("year", datePicker.getValue().getYear());
        Document doc = new Document("Arrival Time", arrivalTime.getValue())
                .append("Time to begin Operation", operationField.getText())
                .append("Duration", DureeOperation.getText())
                .append("Operation Type", operationType)
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
        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Envoyé");
        alert.setHeaderText(null);
        alert.setContentText("Urgence bien envoyée!");
        alert.showAndWait();*/
        Notifications.create()
                .title("Envoyé")
                .text("Urgence bien envoyée!")
                .showInformation();
    }

    private void showDeletedMessage() {
        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Supprimer");
        alert.setHeaderText(null);
        alert.setContentText("Urgence bien supprimée!");
        alert.showAndWait();*/
        Notifications.create()
                .title("Supprimer")
                .text("Urgence bien supprimée!")
                .showInformation();
    }



    private boolean isValidSubmission() {
        // TODO set error css
        boolean isValid = true;
        String errorMessage = "";
        // Patient Dossier number
        Pattern p = Pattern.compile("[0-9]{6}");
        Matcher m = p.matcher(numDossier.getText());
        if (!m.find()) {
            isValid = false;
            errorMessage += "Le numéro de dossier doit être un nombre composé de 6 chiffres\n";
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
        if (operationCombobox.getSelectionModel().isEmpty() ||
                (!autreTextField.isDisabled() && autreTextField.getText().isEmpty())) {
            isValid = false;
            errorMessage += "Veuillez choisir une type d'opération.\n";
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


    public void updateDetailsPane(Urgence urgence) {
        if (urgence == null) {
            // clear values
            clearDetailsView();
            return;
        }

        //detailsDossier.setText(urgence.getDossier());
        detailsMedecin.setText(urgence.getDoctorName());
        detailsChirurgie.setText(urgence.getOperationType());
        detailsHeure.setText(urgence.getPrettyTime());
        detailsDate.setText(urgence.getPrettyDate());
        detailsTempsDeLatence.setText(String.valueOf(urgence.getTimeToBeginOperation()) + "h");
        detailsDuree.setText(String.valueOf(urgence.getDuration()) + "h");
        detailsDeleteButton.setDisable(false);
    }



    public void handleDeleteButton(ActionEvent actionEvent) {
        Urgence urgence = ganttChart.selectedUrgence;
        System.out.println("remove" + urgence);
        DatabaseManager.removeUrgence(urgence);
        clearDetailsView();
        showDeletedMessage();
    }

    private void clearDetailsView() {
        detailsMedecin.setText("");
        detailsChirurgie.setText("");
        detailsHeure.setText("");
        detailsDate.setText("");
        detailsTempsDeLatence.setText("");
        detailsDuree.setText("");
        detailsDeleteButton.setDisable(true);
    }
}
