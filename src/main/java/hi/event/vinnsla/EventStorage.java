package hi.event.vinnsla;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EventStorage {

    private static final String FILE_PATH = "events.json"; // Path to store events file

    // Save events to a JSON file using JsonUtils
    public static void saveEvents(List<Event> events) throws IOException {
        // Validate events before saving
        List<Event> validEvents = EventValidator.validateEvents(events);

        if (validEvents.isEmpty()) {
            System.err.println("No valid events to save.");
            return;  // Exit early if no valid events are available
        }

        // Convert JavaFX properties to plain fields before serialization
        for (Event event : validEvents) {
            event.extractToPlainValues();  // Ensure plain fields are populated
        }

        try {
            // Use JsonUtils to save to file
            JsonUtils.writeEventsToFile(validEvents, FILE_PATH);
            System.out.println("Successfully saved valid events.");
        } catch (IOException e) {
            System.err.println("Error saving events to file: " + e.getMessage());
            throw e; // Rethrow the exception after logging
        }
    }

    // Load events from a JSON file using JsonUtils
    public static List<Event> loadEvents() throws IOException {
        // Check if file exists before loading
        Path filePath = Paths.get(FILE_PATH);
        if (Files.exists(filePath)) {
            try {
                // Try reading the events from the file
                List<Event> events = JsonUtils.readEventsFromFile(FILE_PATH);

                // If no valid events are read (i.e., the file is empty or invalid), return an empty list
                if (events == null || events.isEmpty()) {
                    System.out.println("Events file is empty or corrupted, starting with an empty list.");
                    return new ArrayList<>(); // Return an empty list if deserialization fails
                }

                // Populate JavaFX properties after loading
                for (Event event : events) {
                    event.populateFromPlainValues();
                }

                return events;
            } catch (IOException e) {
                System.err.println("Error reading events from file: " + e.getMessage());
                throw e; // Rethrow the exception after logging
            }
        } else {
            System.out.println("No events file found, starting with an empty list.");
            return new ArrayList<>(); // Return an empty list if the file doesn't exist
        }
    }
}
