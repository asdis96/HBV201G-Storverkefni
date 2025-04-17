package hi.event.vinnsla;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 *  Author       : Ásdís Halldóra L Stefánsdóttir
 *  Email        : ahl4@hi.is
 *  Description  : Handles loading and saving {@link Event} objects to and from a JSON file.
 *
 *
 *****************************************************************************/
public class EventStorage {

    private static final String FILE_PATH = "events.json"; // Path to store events file

    /**
     * Saves a list of {@link Event} objects to a JSON file.
     *
     * @param events the list of events to be saved
     * @throws IOException if an I/O error occurs during file writing
     */
    public static void saveEvents(List<Event> events) throws IOException {
        List<Event> validEvents = EventValidator.validateEvents(events);

        if (validEvents.isEmpty()) {
            System.err.println("No valid events to save.");
            return;
        }

        for (Event event : validEvents) {
            event.extractToPlainValues();
        }

        try {
            JsonUtils.writeEventsToFile(validEvents, FILE_PATH);
            System.out.println("Successfully saved valid events.");
        } catch (IOException e) {
            System.err.println("Error saving events to file: " + e.getMessage());
            throw e;
        }
    }



    /**
     * Loads a list of {@link Event} objects from a JSON file.
     *
     * @return a list of events loaded from the file, or an empty list if the file is missing or invalid
     * @throws IOException if an I/O error occurs during file reading
     */
    public static List<Event> loadEvents() throws IOException {
        Path filePath = Paths.get(FILE_PATH);
        if (Files.exists(filePath)) {
            try {
                List<Event> events = JsonUtils.readEventsFromFile(FILE_PATH);

                if (events == null || events.isEmpty()) {
                    System.out.println("Events file is empty or corrupted, starting with an empty list.");
                    return new ArrayList<>();
                }

                for (Event event : events) {
                    event.populateFromPlainValues();
                }

                return events;
            } catch (IOException e) {
                System.err.println("Error reading events from file: " + e.getMessage());
                throw e;
            }
        } else {
            System.out.println("No events file found, starting with an empty list.");
            return new ArrayList<>();
        }
    }
}
