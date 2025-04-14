package hi.event.vidmot;

import hi.event.vinnsla.Event;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class KynningController {

    @FXML private Button fxStartButton;
    @FXML private Button fxPlayPause;
    @FXML private Button fxEndButton;
    @FXML private VBox overlay;
    @FXML private MediaView fxVideoView;

    private MediaPlayer mediaPlayer;

    // Initialize the controller and disable buttons if no media is present
    public void initialize() {
        fxStartButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> fxVideoView.mediaPlayerProperty().getValue() == null,
                fxVideoView.mediaPlayerProperty()
        ));

        fxPlayPause.disableProperty().bind(Bindings.createBooleanBinding(
                () -> fxVideoView.mediaPlayerProperty().getValue() == null,
                fxVideoView.mediaPlayerProperty()
        ));

        fxEndButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> fxVideoView.mediaPlayerProperty().getValue() == null,
                fxVideoView.mediaPlayerProperty()
        ));


    }

    @FXML
    private void start(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO);  // Rewind to start
        }
    }

    @FXML
    private void end(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getTotalDuration());  // Jump to the end
        }
    }

    @FXML
    private void playPause(ActionEvent event) {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();  // Play the media
            } else {
                mediaPlayer.pause();  // Pause the media
            }
        }
    }

    // Method to set the MediaPlayer and link it to the MediaView
    public void setMediaPlayer(Media media) {
        mediaPlayer = new MediaPlayer(media);  // Initialize MediaPlayer with media
        mediaPlayer.setOnError(() -> System.out.println("Error in player: " + mediaPlayer.getError().getMessage()));

        // Set up media player when ready
        mediaPlayer.setOnReady(() -> {
            mediaPlayer.seek(Duration.ZERO);  // Start from the beginning
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);  // Loop indefinitely
            mediaPlayer.play();  // Start playing

            fxVideoView.setMediaPlayer(mediaPlayer);  // Link the MediaPlayer to the MediaView
            fxVideoView.setVisible(true);  // Ensure the MediaView is visible
        });
    }

    // Stop the MediaPlayer when the dialog is closed
    public void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();   // Stop the playback
            mediaPlayer.dispose(); // Release resources
        }
    }

    // This method updates the event's video media when the event changes
    public void setEventView(NewEventController newEventController) {
        Event event = newEventController.getEvent();
        event.videoMediaProperty().addListener((observable, oldValue, newValue) -> {
            setMediaPlayer(newValue);  // Set the media when the event's media changes
        });
    }

    // Align the overlay (controls) at the bottom-center of the MediaView
    public void setAlignment() {
        StackPane.setAlignment(overlay, Pos.BOTTOM_CENTER);
    }
}

