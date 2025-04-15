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
    private KynningController kynningController;

    public EventView(Event event, Region referenceRegion) {
        this.event = event;

        // Load FXML for EventView
        FXMLLoader loader = new FXMLLoader(getClass().getResource("event-view.fxml"));
        loader.setController(this);  // Set this class as the controller for event-view.fxml
        try {
            DialogPane pane = loader.load();
            this.setDialogPane(pane);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load EventView FXML", e);
        }

        // Now, load the media-view.fxml and let FXML inject the KynningController
        loadMediaViewController();

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

        // Stop media when the dialog is closed
        setOnCloseRequest(closeEvent -> stopMediaPlayback());

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
        String videoPath = event.videoMediaPathProperty().getValue();
        if (videoPath != null && !videoPath.isEmpty()) {
            try {
                URL videoUrl = getClass().getResource(videoPath);
                if (videoUrl != null) {
                    Media videoMedia = new Media(videoUrl.toExternalForm());

                    // Cast the user data of mediaView to KynningController
                    if (kynningController != null) {
                        kynningController.setMediaPlayer(videoMedia);  // Set the media player
                    } else {
                        showError("Error: KynningController is not properly injected.");
                    }
                } else {
                    showError("Video not found: " + videoPath);
                }
            } catch (Exception e) {
                showError("Error loading video: " + e.getMessage());
            }
        } else {
            showError("Video path is empty or null.");
        }
    }


    private void loadMediaViewController() {
        // Load the media-view.fxml to get the controller (KynningController)
        FXMLLoader mediaLoader = new FXMLLoader(getClass().getResource("media-view.fxml"));
        mediaLoader.setControllerFactory(param -> {
            kynningController = new KynningController();  // Create the controller manually
            return kynningController;
        });

        try {
            VBox mediaVBox = mediaLoader.load();  // Load the VBox
            mediaView.getChildren().setAll(mediaVBox.getChildren());  // Add the loaded children to mediaView
        } catch (IOException e) {
            showError("Failed to load media-view.fxml");
        }
    }

    // Method to stop the media playback when the dialog is closed
    private void stopMediaPlayback() {
        if (kynningController != null) {
            kynningController.stopMediaPlayer();  // Call the stop method in KynningController to stop the media
        }
    }

    private void showError(String message) {
        // Handle error, could be logging or a user dialog
        System.out.println(message);
    }
}
