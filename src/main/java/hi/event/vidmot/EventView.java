package hi.event.vidmot;

import hi.event.vinnsla.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EventView extends Dialog<Event> {

    @FXML private Label fxTitle;
    @FXML private Label fxDate;
    @FXML private Label fxTime;
    @FXML private Label fxGroup;
    @FXML private Label fxStatus;
    @FXML private Label fxDescription;
    @FXML private ImageView fxImage;

    // This will hold the MediaView and controls from KynningController
    @FXML private VBox mediaView;  // The VBox that holds KynningController's content

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
        fxTitle.setText(event.getTitle());
        fxDate.setText("Date: " + event.getDate());
        fxTime.setText("Time: " + event.getTime());
        fxGroup.setText("Group: " + event.getGroup());
        fxStatus.setText("Status: " + event.getStatus());
        fxDescription.setText("Description: " + event.getDescription());

        if (event.getImageMedia() != null) {
            fxImage.setImage(event.getImageMedia());
        }

        // Set up video playback if media exists
        if (event.getVideoMedia() != null) {
            // Load the KynningController FXML and link it with the controller
            FXMLLoader kynningLoader = new FXMLLoader(getClass().getResource("media-view.fxml"));

            try {
                // This will automatically load the media-view.fxml and use the controller defined in the FXML
                kynningLoader.load();

                // Set the media for the video player
                KynningController kynningController = kynningLoader.getController();
                kynningController.setMediaPlayer(event.getVideoMedia());

            } catch (IOException e) {
                throw new RuntimeException("Failed to load KynningController FXML", e);
            }
        }

    }
}
