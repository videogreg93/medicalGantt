package sample;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by gregory on 1/26/18.
 */
public class DatabaseManager {
    private static String MONGODB_URL = "mongodb://admin:admin@ds133249.mlab.com:33249/nodetest";
    private static String DATABASE_NAME = "nodetest";
    private static String DEFAULT_COLLECTION_NAME = "test";

    private static MongoDatabase db;
    private static MongoCollection<Document> collection;

    private static DatabaseReference urgenceRef;

    public static void init() {
        // Establish connection
        /*MongoClientURI connectionString = new MongoClientURI(MONGODB_URL);
        MongoClient mongoClient = new MongoClient(connectionString);
        db = mongoClient.getDatabase(DATABASE_NAME);
        collection = db.getCollection(DEFAULT_COLLECTION_NAME);*/

        try {
            FileInputStream serviceAccount =  new FileInputStream("dadproject-bf6ed-firebase-adminsdk-vanjj-a923d8ebf1.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://dadproject-bf6ed.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
            urgenceRef = FirebaseDatabase
                    .getInstance()
                    .getReference("urgence");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void insertNewUrgence(Document doc) {
        //collection.insertOne(doc);

        urgenceRef.push().setValueAsync(doc);

    }
}
