package hi.event.vinnsla;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        // Set working directory explicitly (optional)
        System.setProperty("user.dir", "/Users/asdisstefansdottir/Documents/Skóli/Storverkefni");

        List<Event> events = new ArrayList<>();

        // Add some test events (both valid and invalid)
        events.add(new Event(true, "Valid Event", LocalDate.now(), Group.ENTERTAINMENT, EventStatus.ACTIVE, "world_fixed.mp4", "monk.JPG", LocalTime.now(), "A valid event"));
        events.add(new Event(true, "", LocalDate.now(), Group.ENTERTAINMENT, EventStatus.ACTIVE, "invalid-video.mp4", "invalid-image.jpg", LocalTime.now(), "An invalid event due to empty title"));
        events.add(new Event(true, "Invalid Media Event", LocalDate.now(), Group.ENTERTAINMENT, EventStatus.ACTIVE, "non-existent-video.mp4", "non-existent-image.jpg", LocalTime.now(), "An invalid event due to non-existent media"));

        for (Event event : events) {
            // Print absolute path for debugging
            System.out.println("Checking video file as a resource...");
            URL videoURL = Test.class.getClassLoader().getResource(event.getVideoMediaPathValue());
            System.out.println("Video file found: " + (videoURL != null ? videoURL.getPath() : "File not found"));

            // Similarly check for image path
            URL imageURL = Test.class.getClassLoader().getResource(event.getImageMediaPathValue());
            System.out.println("Image file found: " + (imageURL != null ? imageURL.getPath() : "File not found"));


            // Print absolute paths of video and image
            System.out.println("Absolute path of video: " + new File(event.getVideoMediaPathValue()).getAbsolutePath());
            System.out.println("Absolute path of image: " + new File(event.getImageMediaPathValue()).getAbsolutePath());
        }

        try {
            EventStorage.saveEvents(events);  // This will only save valid events
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
