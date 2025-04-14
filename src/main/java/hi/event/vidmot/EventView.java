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
            this.getDialogPane().setPrefWidth(referenceRegion.getWidth());
            this.getDialogPane().setPrefHeight(referenceRegion.getHeight());
        }

        this.setTitle("Event Details");
        this.setHeaderText("View Event Information");
        this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        populateFields();
    }

    private void populateFields() {
        fxTitle.setText("Title: " + event.getTitle());
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
            KynningController kynningController = new KynningController();  // Create a new controller instance
            kynningLoader.setController(kynningController);  // Set the controller

            try {
                // Get the VBox from KynningController and add it to the mediaView in EventView
                VBox kynningVBox = kynningLoader.load();
                mediaView.getChildren().add(kynningVBox);  // Add KynningController's VBox to EventView

                // Set the media for the video player
                kynningController.setMediaPlayer(event.getVideoMedia());

            } catch (IOException e) {
                throw new RuntimeException("Failed to load KynningController FXML", e);
            }
        }
    }
}
