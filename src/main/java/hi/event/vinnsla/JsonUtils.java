package hi.event.vinnsla;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    // Jackson ObjectMapper instance
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Enable pretty print for JSON output
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: to disable timestamps
    }

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


    // Method to write events to a JSON file
    public static void writeEventsToFile(List<Event> events, String filePath) throws IOException {
        File file = new File(filePath);

        // Serialize the list of events to JSON and write it to the file
        objectMapper.writeValue(file, events);
    }

    // Method to convert an Event object to JSON string (for debugging or logging)
    public static String eventToJsonString(Event event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }

    // Method to convert a list of Event objects to JSON string (for debugging or logging)
    public static String eventsToJsonString(List<Event> events) throws JsonProcessingException {
        return objectMapper.writeValueAsString(events);
    }
}
