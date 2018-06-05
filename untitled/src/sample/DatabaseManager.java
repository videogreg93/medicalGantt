package sample;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.ComboBox;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gregory on 1/26/18.
 */
public class DatabaseManager {
    private static String DATABASE_URL = "https://dadproject-bf6ed.firebaseio.com";
    private static String SERVICE_ACCOUNT = "dadproject-bf6ed-firebase-adminsdk-vanjj-a923d8ebf1.json";
    private static String URGENCE_DATABASE_NAME = "urgence";
    private static String DOCTORS_DATABASE_NAME = "doctors";

    private static DatabaseReference urgenceRef;

    public static void init() {
        // Establish connection
        try {
            FileInputStream serviceAccount =  new FileInputStream(SERVICE_ACCOUNT);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();

            FirebaseApp.initializeApp(options);
            urgenceRef = FirebaseDatabase
                    .getInstance()
                    .getReference(URGENCE_DATABASE_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void insertNewUrgence(Document doc) {
        //collection.insertOne(doc);

        urgenceRef.push().setValueAsync(doc);

    }

    public static void loadDoctorNames(ComboBox<String> comboBox) {
        DatabaseReference doctors = FirebaseDatabase
                .getInstance()
                .getReference(DOCTORS_DATABASE_NAME);
        doctors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Controller.doctors = (HashMap<String, String>) dataSnapshot.getValue();
                ArrayList<String> doctorNames = new ArrayList<>();
                doctorNames.addAll(Controller.doctors.keySet());
                Collections.sort(doctorNames);
                comboBox.getItems().addAll(doctorNames);
                comboBox.setDisable(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void setupUrgenceLoader(GanttChart ganttChart) {
        urgenceRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Urgence urgence = Urgence.createFromMap((Map<String, Object>)dataSnapshot.getValue());
                ganttChart.addUrgence(urgence);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
