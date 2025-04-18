package hi.event.vinnsla;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 *
 *  Description  : Utility class for validating {@link Event} objects.
 * Provides methods to validate individual events as well as lists of events.
 *
 *
 *****************************************************************************/
public class EventValidator {

    private static final String MEDIA_BASE_PATH = "src/main/resources/hi/event/vidmot/";

    /**
     * Validates a single {@link Event} object.
     * This method checks that all required fields (title, description, date, time)
     * are not null or empty and that the media files (video and image) exist at the specified paths.
     *
     * @param event the {@link Event} object to validate
     * @return true if the event is valid, false otherwise
     */
    public static boolean isValid(Event event) {
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

        if (!isMediaFileValid(event.getVideoMediaPathValue())) {
            return false;
        }

        if (!isMediaFileValid(event.getImageMediaPathValue())) {
            return false;
        }

        return true;
    }

    /**
     * Checks if a media file exists at the specified relative path.
     *
     * @param mediaFilePath the relative path to the media file
     * @return true if the file exists, false otherwise
     */
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

    /**
     * Validates a list of {@link Event} objects.
     * This method checks each event in the list and returns only the valid events.
     *
     * @param events the list of {@link Event} objects to validate
     * @return a list of valid {@link Event} objects
     */
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
