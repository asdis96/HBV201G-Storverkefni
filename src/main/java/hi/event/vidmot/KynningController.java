package hi.event.vidmot;

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

/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 *
 *  Description  : 
 *
 *
 *****************************************************************************/

public class KynningController {

    @FXML
    private Button fxStartButton;
    @FXML private Button fxPlayPause;
    @FXML private Button fxEndButton;
    @FXML private VBox overlay;
    @FXML private Button fxMuteButton;
    @FXML private MediaView fxVideoView;

    private MediaPlayer mediaPlayer;

    // Initialize the controller and disable buttons if no media is present
    public void initialize() {
        // Disable the buttons if no media is set
        fxStartButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> mediaPlayer == null,
                fxVideoView.mediaPlayerProperty()
        ));

        fxPlayPause.disableProperty().bind(Bindings.createBooleanBinding(
                () -> mediaPlayer == null,
                fxVideoView.mediaPlayerProperty()
        ));

        fxEndButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> mediaPlayer == null,
                fxVideoView.mediaPlayerProperty()
        ));
        fxMuteButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> mediaPlayer == null,
                fxVideoView.mediaPlayerProperty()
        ));

        // Set up the play/pause button action
        fxPlayPause.setOnAction(this::playPause);
        fxStartButton.setOnAction(this::start);
        fxEndButton.setOnAction(this::end);
        fxMuteButton.setOnAction(this::toggleMute);
    }

    // Start the video from the beginning (rewind)
    @FXML
    private void start(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO);  // Rewind to start
            mediaPlayer.play();  // Start playing after rewinding
        }
    }

    // Jump to the end of the video
    @FXML
    private void end(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getTotalDuration());  // Jump to the end
            mediaPlayer.pause();  // Pause at the end of the video
        }
    }

    // Play or pause the video based on the current state
    @FXML
    private void playPause(ActionEvent event) {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED || mediaPlayer.getStatus() == MediaPlayer.Status.READY) {
                mediaPlayer.play();  // Play the media
                fxPlayPause.setText("Pause");  // Update button text
            } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();  // Pause the media
                fxPlayPause.setText("Play");  // Update button text
            }
        }
    }
    // Mute or unmute the media
    @FXML
    private void toggleMute(ActionEvent event) {
        if (mediaPlayer != null) {
            boolean isMuted = mediaPlayer.isMute();
            mediaPlayer.setMute(!isMuted); // Toggle the mute state
            fxMuteButton.setText(isMuted ? "Mute" : "Unmute"); // Update button text
        }
    }

    public void setMediaPlayer(Media media) {
        if (media == null) return;  // Handle case where media is null

        // Create a new MediaPlayer for the video media
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnError(() -> System.out.println("Error in player: " + mediaPlayer.getError().getMessage()));

        // Set up media player when ready
        mediaPlayer.setOnReady(() -> {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);  // Loop indefinitely
            fxVideoView.setMediaPlayer(mediaPlayer);  // Link the MediaPlayer to the MediaView
            fxVideoView.setVisible(true); // Ensure the MediaView is visible
            mediaPlayer.setMute(true);  // Initially mute the media
            fxMuteButton.setText("Unmute");
            mediaPlayer.play();  // Start playing the video
        });
    }


    // Stop the MediaPlayer when the dialog is closed or when done
    public void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();  // Stop the playback
            mediaPlayer.dispose(); // Release resources
        }
    }


    // Align the overlay (controls) at the bottom-center of the MediaView
    public void setAlignment() {
        StackPane.setAlignment(overlay, Pos.BOTTOM_CENTER);
    }
}
