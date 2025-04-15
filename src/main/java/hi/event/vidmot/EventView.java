package hi.event.vidmot;

import hi.event.vinnsla.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.IOException;
import java.net.URL;

public class EventView extends Dialog<Event> {

    @FXML
    private Label fxTitle;
    @FXML
    private Label fxDate;
    @FXML
    private Label fxTime;
    @FXML
    private Label fxGroup;
    @FXML
    private Label fxStatus;
    @FXML
    private Label fxDescription;
    @FXML
    private ImageView fxImage;

    // This will hold the MediaView and controls from KynningController
    @FXML
    private VBox mediaView;  // The VBox that holds KynningController's content

    private Event event;

    public EventView(Event event, Region referenceRegion) {
        this.event = event;

        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("event-view.fxml"));
        loader.setController(this);
        try {
            DialogPane pane = loader.load();
            this.setDialogPane(pane);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load EventView FXML", e);
        }

        // Set size like Event Table
        if (referenceRegion != null) {
            this.getDialogPane().setPrefWidth(referenceRegion.getWidth() * 0.8);  // 80% of the reference width
            this.getDialogPane().setPrefHeight(referenceRegion.getHeight() * 0.8);  // 80% of the reference height
        } else {
            // Set default sizes if referenceRegion is null
            this.getDialogPane().setPrefWidth(580);  // Set a fixed width
            this.getDialogPane().setPrefHeight(400);  // Set a fixed height
        }

        // Set maximum size for the dialog if needed
        this.getDialogPane().setMaxWidth(600);  // Max width
        this.getDialogPane().setMaxHeight(600); // Max height


        this.setTitle("Event Details");
        this.setHeaderText("View Event Information");
        this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        populateFields();
    }

    private void populateFields() {
        // Set title and description
        fxTitle.setText(event.titleProperty().getValue());
        fxDescription.setText("Description: " + event.descriptionProperty().getValue());

        // Use the formatted date and time values for fxDate and fxTime
        fxDate.setText("Date: " + event.getFormattedDateForFXML());
        fxTime.setText("Time: " + event.getTimeValue().toString());  // You can also format the time if needed

        // Set group and status
        fxGroup.setText("Group: " + event.groupProperty().getValue());
        fxStatus.setText("Status: " + event.statusProperty().getValue());

        // Load the image using the loadImageMedia() method from Event class
        loadImageMedia();  // This will load the image based on the imageMediaPath
        loadVideoMedia();  // This will load the video based on the videoMediaPath
    }

    private void loadImageMedia() {
        String imagePath = event.imageMediaPathProperty().getValue();  // Get the relative path for the image
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                // Access the image using getClass().getResource() for classpath resources
                URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    // Create the Image object using the URL from the classpath
                    Image image = new Image(imageUrl.toExternalForm());
                    fxImage.setImage(image);  // Set the image to the UI element
                    event.imageMediaProperty().set(image);  // Set JavaFX property value
                } else {
                    showError("Image not found: " + imagePath);
                }
            } catch (Exception e) {
                showError("Error loading image: " + e.getMessage());
            }
        }
    }

    private void loadVideoMedia() {
        String videoPath = event.videoMediaPathProperty().getValue();  // Get the relative path for the video
        if (videoPath != null && !videoPath.isEmpty()) {
            try {
                // Access the video using getClass().getResource() for classpath resources
                URL videoUrl = getClass().getResource(videoPath);
                if (videoUrl != null) {
                    // Create the Media object using the URL from the classpath
                    Media videoMedia = new Media(videoUrl.toExternalForm());

                    // Create the MediaPlayer and MediaView
                    MediaPlayer mediaPlayer = new MediaPlayer(videoMedia);
                    MediaView mediaViewComponent = new MediaView(mediaPlayer);

                    // Add the MediaView component to the VBox
                    mediaView.getChildren().add(mediaViewComponent);

                    // Start playing the media
                    mediaPlayer.play();
                } else {
                    showError("Video not found: " + videoPath);
                }
            } catch (Exception e) {
                showError("Error loading video: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        System.out.println(message);  // Display error message or handle it in a dialog
    }
}
