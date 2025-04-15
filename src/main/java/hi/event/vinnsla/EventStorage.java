package hi.event.vinnsla;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventStorage {
    private static final String FILE_PATH = "events.json"; // Path to store events file

    public static void saveEvents(List<Event> events) throws IOException {
        // Create a JSONArray to hold all event objects
        JSONArray eventArray = new JSONArray();

        // The base path for media files in resources
        Path basePath = Paths.get("src", "main", "resources", "hi", "event", "vidmot", "media");
        if (Files.notExists(basePath)) {
            basePath = Paths.get("target", "classes", "hi", "event", "vidmot", "media");
        }

        for (Event event : events) {
            JSONObject eventObj = new JSONObject();
            eventObj.put("selected", event.getSelected());
            eventObj.put("title", event.getTitle());
            eventObj.put("date", event.getDate().toString());
            eventObj.put("group", event.getGroup().toString());
            eventObj.put("status", event.getStatus().toString());

            // Convert absolute paths to relative paths for both video and image
            String videoMediaPath = "";
            if (event.getVideoMedia() != null && event.getVideoMedia().getSource() != null) {
                videoMediaPath = getRelativePath(basePath, Paths.get(event.getVideoMedia().getSource()));
            }

            String imageMediaPath = "";
            if (event.getImageMedia() != null && event.getImageMedia().getUrl() != null) {
                imageMediaPath = getRelativePath(basePath, Paths.get(event.getImageMedia().getUrl()));
            }

            eventObj.put("videoMediaPath", videoMediaPath);
            eventObj.put("imageMediaPath", imageMediaPath);

            eventObj.put("time", event.getTime().toString());
            eventObj.put("description", event.getDescription());

            eventArray.put(eventObj);
        }

        // Write the JSON array to the file
        Files.write(Paths.get(FILE_PATH), eventArray.toString().getBytes());
    }


    /**
     * Converts an absolute path to a relative path based on a given base path.
     */
    private static String getRelativePath(Path basePath, Path absolutePath) {
        // Convert the paths to strings
        String basePathString = basePath.toString().replace("\\", "/");
        String absolutePathString = absolutePath.toString().replace("\\", "/");

        // Check if the absolute path contains "src/main/resources"
        String resourcesPath = "src/main/resources/";
        int startIndex = absolutePathString.indexOf(resourcesPath);

        if (startIndex == -1) {
            // Check if the absolute path contains "target/classes"
            resourcesPath = "target/classes/";
            startIndex = absolutePathString.indexOf(resourcesPath);

            if (startIndex == -1) {
                throw new IllegalArgumentException("The absolute path does not contain the resources or classes directory.");
            }
        }

        // Extract the part of the path starting from the appropriate location
        String relativePath = absolutePathString.substring(startIndex + resourcesPath.length());

        // Ensure the path uses Unix-style slashes
        return relativePath.replace("\\", "/");
    }

    public static List<Event> loadEvents() throws IOException {
        List<Event> events = new ArrayList<>();
        Path filePath = Paths.get(FILE_PATH);

        // Use ClassLoader to load resources in a static method
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Check if the file exists
        if (!Files.exists(filePath)) {
            System.out.println("No events file found, starting with an empty list.");
        } else {
            String content = new String(Files.readAllBytes(filePath));
            if (content.trim().isEmpty() || content.equals("[]")) {
                System.out.println("The events file is empty, returning an empty list.");
            } else {
                JSONArray eventArray = new JSONArray(content);

                for (int i = 0; i < eventArray.length(); i++) {
                    JSONObject eventObj = eventArray.getJSONObject(i);
                    Boolean selected = eventObj.getBoolean("selected");
                    String title = eventObj.getString("title");
                    LocalDate date = LocalDate.parse(eventObj.getString("date"));
                    String groupString = eventObj.getString("group");
                    String statusString = eventObj.getString("status");
                    String videoMedia = eventObj.getString("videoMediaPath");
                    String imageMedia = eventObj.getString("imageMediaPath");
                    LocalTime time = LocalTime.parse(eventObj.getString("time"));
                    String description = eventObj.getString("description");

                    // Get media paths using ClassLoader for loading resources
                    Path videoMediaPath = null;
                    Path imageMediaPath = null;

                    try {
                        // Ensure the media paths are correct, and use the appropriate ClassLoader call
                        videoMediaPath = loadMediaFile(classLoader, "media/" + videoMedia);
                        imageMediaPath = loadMediaFile(classLoader, "media/" + imageMedia);
                    } catch (Exception e) {
                        System.err.println("Error loading media files: " + e.getMessage());
                        continue;  // Skip the event if media files can't be loaded
                    }

                    // Convert group and status strings to enums
                    Group group = Group.valueOf(groupString.trim().toUpperCase());
                    EventStatus status = EventStatus.valueOf(statusString.trim().toUpperCase());

                    // Create event object
                    Event event = new Event(
                            selected,
                            title,
                            date,
                            group,
                            status,
                            new Media(videoMediaPath.toUri().toString()),  // Video Media
                            new Image(imageMediaPath.toUri().toString()),  // Image Media
                            time,
                            description,
                            videoMediaPath.toString(),
                            imageMediaPath.toString()
                    );
                    events.add(event);
                }
            }
        }

        return events;
    }


    /**
     * Utility method to load media file from resources using ClassLoader.
     */
    private static Path loadMediaFile(ClassLoader classLoader, String filePath) throws Exception {
        URL resourceUrl = classLoader.getResource(filePath);
        if (resourceUrl == null) {
            throw new Exception("Resource not found: " + filePath);
        }
        return Paths.get(resourceUrl.toURI());
    }
}
