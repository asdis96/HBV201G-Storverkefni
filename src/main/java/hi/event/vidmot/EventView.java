package hi.event.vidmot;

import hi.event.vinnsla.Event;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;

import java.io.IOException;
import java.net.URL;

/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email     : ahl4@hi.is
 *
 *  Description  : EventView is a custom Dialog that displays detailed
 *  information about an event, including media content like images and videos.
 *
 *****************************************************************************/

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
    @FXML
    private VBox mediaView;  // The VBox that holds MediaController's content

    @FXML
    private ScrollPane scrollPane;


    private Event event;
    private MediaController mediaController;


    /**
     * Constructs an EventView dialog for displaying event details.
     * @param event The event to display in the dialog.
     * @param referenceRegion A reference region to determine dialog size, can be null.
     */
    public EventView(Event event, Region referenceRegion) {
        this.event = event;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("event-view.fxml"));
        loader.setController(this);
        try {
            DialogPane pane = loader.load();
            this.setDialogPane(pane);

            // Apply theme to the dialog's scene
            Platform.runLater(() -> {
                Scene scene = this.getDialogPane().getScene();
                if (scene != null) {
                    String currentTheme = EventManagerApplication.getTheme();
                    scene.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());
                }
            });

        } catch (IOException e) {
            throw new RuntimeException("Failed to load EventView FXML", e);
        }

        loadMediaViewController();

        this.getDialogPane().setMaxWidth(700);
        this.getDialogPane().setMaxHeight(650);

        this.setTitle("Event Details");
        this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        populateFields();
        setOnCloseRequest(closeEvent -> stopMediaPlayback());
    }


    /**
     * Populates the fields in the EventView dialog with event data.
     */
    private void populateFields() {
        fxTitle.setText(event.titleProperty().getValue());
        fxDescription.setText("Description: " + event.descriptionProperty().getValue());
        fxDate.setText("Date: " + event.getFormattedDateForFXML());
        fxTime.setText("Time: " + event.getTimeValue().toString());
        fxGroup.setText("Group: " + event.groupProperty().getValue());
        fxStatus.setText("Status: " + event.statusProperty().getValue());

        loadImageMedia();
        loadVideoMedia();
    }

    /**
     * Loads the image media associated with the event and sets it to the ImageView.
     */
    private void loadImageMedia() {
        String imagePath = event.imageMediaPathProperty().getValue();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {

                URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {

                    Image image = new Image(imageUrl.toExternalForm());
                    fxImage.setImage(image);
                    event.imageMediaProperty().set(image);
                } else {
                    showError("Image not found: " + imagePath);
                }
            } catch (Exception e) {
                showError("Error loading image: " + e.getMessage());
            }
        }
    }

    /**
     * Loads the video media associated with the event and sets it in the MediaController.
     */
    private void loadVideoMedia() {
        String videoPath = event.videoMediaPathProperty().getValue();
        if (videoPath != null && !videoPath.isEmpty()) {
            try {
                URL videoUrl = getClass().getResource(videoPath);
                if (videoUrl != null) {
                    Media videoMedia = new Media(videoUrl.toExternalForm());
                    if (mediaController != null) {
                        mediaController.setMediaPlayer(videoMedia);
                    } else {
                        showError("Error: MediaController is not properly injected.");
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

    /**
     * Loads the media-view.fxml and sets up the MediaController to manage media playback.
     */
    private void loadMediaViewController() {
        FXMLLoader mediaLoader = new FXMLLoader(getClass().getResource("media-view.fxml"));
        mediaLoader.setControllerFactory(param -> {
            mediaController = new MediaController();
            return mediaController;
        });

        try {
            VBox mediaVBox = mediaLoader.load();
            mediaView.getChildren().setAll(mediaVBox.getChildren());
        } catch (IOException e) {
            showError("Failed to load media-view.fxml");
        }
    }

    /**
     * Stops media playback when the dialog is closed.
     */
    private void stopMediaPlayback() {
        if (mediaController != null) {
            mediaController.stopMediaPlayer();
        }
    }

    /**
     * Displays an error message to the console or logs it.
     * @param message The error message to display.
     */
    private void showError(String message) {
        System.out.println(message);
    }
}
