package hi.event.vinnsla;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EventStorage {

    private static final String FILE_PATH = "events.json"; // Path to the file where events will be stored

    // Save events to a JSON file
    public static void saveEvents(ObservableList<Event> events) {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(FILE_PATH)) {
            // Convert ObservableList to List
            List<Event> eventList = events; // ObservableList is a subclass of List, so this works directly
            gson.toJson(eventList, writer);
        } catch (IOException e) {
            e.printStackTrace(); // Log the error for debugging
        }
    }

    // Load events from a JSON file
    public static ObservableList<Event> loadEvents() {
        Gson gson = new Gson();
        try {
            // Check if the file exists
            if (!Files.exists(Paths.get(FILE_PATH))) {
                // If the file doesn't exist, return an empty ObservableList
                return FXCollections.observableArrayList();
            }

            // Read the content of the file
            String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Type eventListType = new TypeToken<List<Event>>() {}.getType();
            List<Event> eventList = gson.fromJson(json, eventListType);

            // Check if the eventList is null, handle it gracefully
            if (eventList == null) {
                eventList = List.of();  // If deserialization returned null, initialize an empty list
            }

            // Use `setSelectedFromSerialization` to convert back to SimpleBooleanProperty
            for (Event event : eventList) {
                event.setSelectedFromSerialization(event.getSelectedForSerialization());
            }

            return FXCollections.observableArrayList(eventList); // Convert List back to ObservableList
        } catch (IOException e) {
            e.printStackTrace(); // Log the error for debugging
            return FXCollections.observableArrayList();  // Return empty list in case of error
        } catch (Exception e) {
            e.printStackTrace(); // Catch other unexpected exceptions
            return FXCollections.observableArrayList(); // Return empty list
        }
    }
}
