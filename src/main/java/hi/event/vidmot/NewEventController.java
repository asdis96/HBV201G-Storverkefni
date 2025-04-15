package hi.event.vidmot;

import hi.event.vinnsla.Event;
import hi.event.vinnsla.EventStatus;
import hi.event.vinnsla.EventStorage;
import hi.event.vinnsla.Group;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

/******************************************************************************
 *  Nafn    : Ásdís Stefánsdóttir
 *  T-póstur: ahl4@hi.is
 *
 *  Lýsing  :
 *****************************************************************************/
public class NewEventController extends VBox {

    @FXML
    private DatePicker fxDate;
    @FXML
    public ImageView fxImage;
    @FXML
    public MediaView fxMediaView;

    @FXML
    private ComboBox<Group> fxGroup;

    @FXML
    private ComboBox<EventStatus> fxStatus;

    @FXML
    private TextField fxTitle;

    @FXML
    private Spinner<Integer> fxTime;

    @FXML
    private Button fxCancelButton;

    @FXML
    private TextArea fxDescription;

    @FXML
    private Button fxSaveButton;

    @FXML
    private FileChooser fxFileChooser = new FileChooser();


    @FXML
    private Event event = new Event(
            false,                     // selected
            "",                         // title
            LocalDate.now(),            // date
            Group.ENTERTAINMENT,        // group
            EventStatus.ACTIVE,         // status
            null,                       // video media (null initially)
            null,                       // image media (null initially)
            LocalTime.now(),            // time
            "",                         // description
            "",                         // video media path (empty initially)
            ""                          // image media path (empty initially)
    ); // New Event by default

    private boolean isNewEvent = true;

    private EventManagerController controller;
    private EventStorage eventStorage;  // Event storage for saving and loading events


    public void setEditMode(EventManagerController controller, Event event) {
        this.controller = controller;
        this.event = event;
        this.isNewEvent = false;

        populateFormWithEventDetails(); // Only needed for edit mode
    }


    public void setController(EventManagerController controller) {
        this.controller = controller;
        this.isNewEvent = true;
        initialize(); // Manual call because initialize won't auto-fire when controller is loaded via FXML
    }


    // Initialize the form with the event details
    public void initialize() {
        // Initialize ComboBoxes
        fxGroup.setItems(FXCollections.observableArrayList(Group.values()));
        fxStatus.setItems(FXCollections.observableArrayList(EventStatus.values()));
        fxStatus.getSelectionModel().select(EventStatus.ACTIVE); // Set default status as ACTIVE

        // Bind properties from the event object
        fxTitle.textProperty().bindBidirectional(event.titleProperty());
        fxGroup.valueProperty().bindBidirectional(event.groupProperty());
        fxDate.valueProperty().bindBidirectional(event.dateProperty());
        fxTime.valueProperty().addListener((obs, oldVal, newVal) -> event.setTime(LocalTime.of(newVal, 0))); // Set event time
        fxDescription.textProperty().bindBidirectional(event.descriptionProperty()); // Bind description field


        fxTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour())); // Default to current hour

        // Set up the time spinner
        setUpTimeSpinner();

        // If this is an existing event (edit), populate the form
        if (!isNewEvent) {
            populateFormWithEventDetails();
        }
    }

    // Set up the spinner for selecting the time of the event
    private void setUpTimeSpinner() {
        fxTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12)); // Default to 12 hours

        // When the time spinner changes, update the event's time
        fxTime.valueProperty().addListener((obs, oldVal, newVal) ->
                event.timeProperty().set(LocalTime.of(newVal, 0))
        );

        // When the event's time changes, update the spinner
        event.timeProperty().addListener((obs, oldTime, newTime) ->
                fxTime.getValueFactory().setValue(newTime.getHour()));
    }

    // Populate the form with event details if editing
    private void populateFormWithEventDetails() {
        // Set the values for the fields based on the existing event
        fxTitle.setText(event.getTitle());
        fxGroup.setValue(event.getGroup());
        fxDate.setValue(event.getDate());
        fxTime.getValueFactory().setValue(event.getTime().getHour());
        fxDescription.setText(event.getDescription());

        // Set the event status
        fxStatus.getSelectionModel().select(event.getStatus());

        // Set the image for editing (if there is an image)
        if (event.getImageMedia() != null) {
            fxImage.setImage(event.getImageMedia());
        }

        // Set the media for editing (if there is media)
        if (event.getVideoMedia() != null) {
            // Create a MediaPlayer from the Media
            Media media = event.getVideoMedia();
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            // Set the MediaPlayer to the MediaView
            fxMediaView.setMediaPlayer(mediaPlayer);

            // Optionally, you can start playing the video
            mediaPlayer.play();
        }

    }

    @FXML
    void openImage(ActionEvent actionEvent) {
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));
        File file = fxFileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                Path selectedFile = file.toPath();

                // Convert the absolute path to a relative path in the resources folder
                String relativeImagePath = "media/" + selectedFile.getFileName().toString(); // Adjust if needed

                // Load the image as a resource
                Image image = new Image(getClass().getResource(relativeImagePath).toExternalForm());

                fxImage.setImage(image);

                // Save the relative path for later use
                this.event.setImageMedia(image);
                this.event.setImageMediaPath(relativeImagePath);
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        } else {
            showError("No image selected.");
        }
    }

    @FXML
    void openMedia(ActionEvent actionEvent) {
        // Clear any previous filters
        fxFileChooser.getExtensionFilters().clear();

        // Add new filters for video and audio files
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv", "*.mov"));
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.flac"));

        // Show the file chooser dialog
        File file = fxFileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                Path selectedFile = file.toPath();

                // Convert the absolute path to a relative path in the resources folder
                String relativeMediaPath = "media/" + selectedFile.getFileName().toString(); // Adjust if needed

                // Load the media as a resource
                Media media = new Media(file.toURI().toString());

                // Set up the media player
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                fxMediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();  // Play the media automatically

                // Save the relative path for later use
                this.event.setVideoMedia(media);
                this.event.setVideoMediaPath(relativeMediaPath); // Save the relative path
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        } else {
            showError("No media selected.");
        }
    }



    private void showError(String message) {
        // Show an error alert with the provided message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid File Selection");
        alert.setContentText(message);
        alert.showAndWait();
    }




    @FXML
    void saveEvent(ActionEvent event) {
        if (this.event != null) {
            if (isNewEvent) {
                // Use the EventManagerController to add the new event
                controller.addEvent(this.event);  // Let the controller handle the event addition
            } else {
                // Use the EventManagerController to update the existing event
                controller.updateEvent(this.event);  // Let the controller handle the event update
            }
            System.out.println("Event saved: " + this.event.getTitle());

            // Show a success alert after saving the event
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Event Saved");
            alert.setHeaderText("Event saved successfully!");
            alert.setContentText("Your event has been saved.");
            alert.showAndWait();

            // Close the dialog window
            closeWindowOnSave();
        } else {
            System.out.println("No event to save.");
        }
    }




    // Handle the Cancel button click, asking for confirmation to discard changes
    @FXML
    void handleCancel(ActionEvent event) {
        // Create the confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Event");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("All changes will be discarded.");

        // Custom buttons
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Show and wait for response
        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                // Close the window without saving changes
                closeWindow();
            }
        });
    }

    // Close the current window (discard changes)
    private void closeWindow() {
        Stage stage = (Stage) fxCancelButton.getScene().getWindow();
        stage.close(); // Close the window and discard changes
    }

    // Close the current window after saving the event
    private void closeWindowOnSave() {
        Stage stage = (Stage) fxSaveButton.getScene().getWindow();
        stage.close(); // Close the window immediately
    }



    // Helper method to view the component
    @Override
    public String toString() {
        return "NewEventController{" +
                ", fxHeiti=" + fxTitle.getText() +
                "} " + super.toString();
    }

    // Get the created Event object
    public Event getEvent() {
        return event;
    }
}
