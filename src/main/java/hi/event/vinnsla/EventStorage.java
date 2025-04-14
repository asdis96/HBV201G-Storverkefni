package hi.event.vinnsla;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class EventStorage {

    private static final String FILE_PATH = "events.json"; // Path to the JSON file

    // Save events to a JSON file
    public static void saveEvents(ObservableList<Event> events) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            // Convert ObservableList to List before saving
            List<Event> eventList = FXCollections.observableArrayList(events);  // Ensure it's a regular List
            gson.toJson(eventList, writer); // Serialize the event list to JSON and save to file
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error saving events", "An error occurred while saving the event data.", e.getMessage());
        }
    }

    // Load events from a JSON file
    public static ObservableList<Event> loadEvents() {
        Gson gson = new Gson();
        File jsonFile = new File(FILE_PATH);
        // Check if the file exists before attempting to read
        if (!jsonFile.exists()) {
            System.out.println("No event data found, starting with an empty list.");
            return FXCollections.observableArrayList(); // Return an empty list if the file doesn't exist
        }

        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type eventListType = new TypeToken<List<Event>>(){}.getType(); // Define the type for list of events
            List<Event> events = gson.fromJson(reader, eventListType); // Deserialize JSON back to a List of Event objects
            return FXCollections.observableArrayList(events); // Convert List to ObservableList
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            showErrorAlert("Error loading events", "An error occurred while loading the event data.", e.getMessage());
            return FXCollections.observableArrayList(); // Return an empty list if loading fails
        }
    }

    // Helper method to show error alerts
    private static void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Show the alert and wait for user confirmation
        alert.showAndWait();
    }
}
