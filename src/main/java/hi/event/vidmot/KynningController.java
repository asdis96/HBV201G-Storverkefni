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

    public Button fxStartButton;
    public Button fxPlayPause;
    public Button fxEndButton;
    public VBox overlay;

    @FXML
    private MediaView fxVideoView;

    /**
     * Initialization of controller. Disable buttons if no media is available.
     */
    public void initialize() {
        fxStartButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> fxVideoView.getMediaPlayer() == null,
                fxVideoView.mediaPlayerProperty()
        ));

        fxPlayPause.disableProperty().bind(Bindings.createBooleanBinding(
                () -> fxVideoView.getMediaPlayer() == null,
                fxVideoView.mediaPlayerProperty()
        ));

        fxEndButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> fxVideoView.getMediaPlayer() == null,
                fxVideoView.mediaPlayerProperty()
        ));
    }

    /**
     * Jump to the start of the media.
     * @param event not used
     */
    @FXML
    void start(ActionEvent event) {
        MediaPlayer mediaPlayer = getMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO);
        }
    }

    /**
     * Jump to the end of the media.
     * @param event not used
     */
    @FXML
    void end(ActionEvent event) {
        MediaPlayer mediaPlayer = getMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getTotalDuration());
        }
    }

    /**
     * Toggle play/pause.
     * @param event the play/pause button action
     */
    @FXML
    void playPause(ActionEvent event) {
        MediaPlayer mediaPlayer = getMediaPlayer();
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
            } else {
                mediaPlayer.pause();
            }
        }
    }

    /**
     * Set a new MediaPlayer for the media.
     * @param media the media to play
     */
    public void setMediaPlayer(Media media) {
        // Create a new MediaPlayer for the provided media
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnError(() -> System.out.println("Error in player: " + mediaPlayer.getError().getMessage()));

        // Set up media player when ready
        mediaPlayer.setOnReady(() -> {
            mediaPlayer.seek(Duration.ZERO);  // Start from the beginning
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);  // Loop indefinitely
            mediaPlayer.play();  // Start playing

            fxVideoView.setMediaPlayer(mediaPlayer);  // Link MediaPlayer to MediaView

            // Bind play/pause button text to media player status
            fxPlayPause.textProperty().bind(Bindings.createStringBinding(this::bindPlayPause, mediaPlayer.statusProperty()));
        });
    }

    /**
     * Set the event's video when it changes.
     * @param newEvent the event containing the media
     */
    public void setEventView(NewEvent newEvent) {
        Event event = newEvent.getEvent();
        event.videoMediaProperty().addListener((observable, oldValue, newValue) -> {
            setMediaPlayer(newValue);  // Set the media when the event's media changes
        });
    }

    /**
     * Align the overlay (controls) at the bottom-center.
     */
    public void setAlignment() {
        StackPane.setAlignment(overlay, Pos.BOTTOM_CENTER);
    }

    /**
     * Helper method to return the current media player.
     * @return the media player of the MediaView
     */
    private MediaPlayer getMediaPlayer() {
        return fxVideoView.getMediaPlayer();
    }

    /**
     * Bind the text of the play/pause button depending on the media player's status.
     * @return the text to display on the button
     */
    private String bindPlayPause() {
        MediaPlayer mediaPlayer = fxVideoView.getMediaPlayer();
        MediaPlayer.Status status = mediaPlayer != null ? mediaPlayer.getStatus() : null;
        return status == MediaPlayer.Status.PLAYING ? "||" : ">";  // Display play or pause symbol
    }
}
