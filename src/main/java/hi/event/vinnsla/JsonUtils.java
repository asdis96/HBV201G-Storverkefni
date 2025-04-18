package hi.event.vinnsla;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 *
 *  Description  :Utility class for handling JSON operations related to {@link Event} objects.
 * Provides methods for reading, writing, and converting {@link Event} objects to/from JSON.
 *
 *
 *****************************************************************************/

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: to disable timestamps
    }

    /**
     * Reads a list of {@link Event} objects from a JSON file.
     *
     * @param filePath the path to the JSON file
     * @return a list of {@link Event} objects read from the file
     * @throws IOException if an I/O error occurs or the JSON format is invalid
     */
    public static List<Event> readEventsFromFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            System.out.println("File is empty or does not exist. Returning an empty list.");
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Event.class));
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing the JSON file: " + e.getMessage());
            throw new IOException("Invalid JSON format.", e);
        }
    }


    /**
     * Writes a list of {@link Event} objects to a JSON file.
     *
     * @param events   the list of {@link Event} objects to write to the file
     * @param filePath the path to the JSON file
     * @throws IOException if an I/O error occurs during writing
     */
    public static void writeEventsToFile(List<Event> events, String filePath) throws IOException {
        File file = new File(filePath);
        objectMapper.writeValue(file, events);
    }

    /**
     * Converts an {@link Event} object to a JSON string for debugging or logging purposes.
     *
     * @param event the {@link Event} object to convert
     * @return the JSON string representation of the event
     * @throws JsonProcessingException if an error occurs during conversion
     */
    public static String eventToJsonString(Event event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }

    /**
     * Converts a list of {@link Event} objects to a JSON string for debugging or logging purposes.
     *
     * @param events the list of {@link Event} objects to convert
     * @return the JSON string representation of the events
     * @throws JsonProcessingException if an error occurs during conversion
     */
    public static String eventsToJsonString(List<Event> events) throws JsonProcessingException {
        return objectMapper.writeValueAsString(events);
    }
}
