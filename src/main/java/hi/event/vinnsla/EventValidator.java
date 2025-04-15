package hi.event.vinnsla;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EventValidator {

    // Base directory for media files
    private static final String MEDIA_BASE_PATH = "src/main/resources/hi/event/vidmot/";

    // Validate a single Event object
    public static boolean isValid(Event event) {
        // Validate required fields
        if (event.getTitleValue() == null || event.getTitleValue().isEmpty()) {
            System.err.println("Invalid event: Title is required.");
            return false;
        }

        if (event.getDescriptionValue() == null || event.getDescriptionValue().isEmpty()) {
            System.err.println("Invalid event: Description is required.");
            return false;
        }

        if (event.getDateValue() == null) {
            System.err.println("Invalid event: Date is required.");
            return false;
        }

        if (event.getTimeValue() == null) {
            System.err.println("Invalid event: Time is required.");
            return false;
        }

        // Check if video media file exists using relative path
        if (!isMediaFileValid(event.getVideoMediaPathValue())) {
            return false;
        }

        // Check if image media file exists using relative path
        if (!isMediaFileValid(event.getImageMediaPathValue())) {
            return false;
        }

        return true;  // Event is valid if all checks passed
    }

    // Check if a media file is valid
    private static boolean isMediaFileValid(String mediaFilePath) {
        if (mediaFilePath != null && !mediaFilePath.isEmpty()) {
            String filePath = Paths.get(MEDIA_BASE_PATH, mediaFilePath).toString();
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("Invalid event: Media file does not exist at path: " + filePath);
                return false;
            }
        }
        return true;
    }

    // Validate a list of events
    public static List<Event> validateEvents(List<Event> events) {
        List<Event> validEvents = new ArrayList<>();
        for (Event event : events) {
            if (isValid(event)) {
                validEvents.add(event);  // Add only valid events to the list
            }
        }
        return validEvents;
    }
}
