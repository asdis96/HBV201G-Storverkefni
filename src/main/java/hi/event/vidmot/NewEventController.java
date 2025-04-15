package hi.event.vidmot;

import hi.event.vinnsla.Event;
import hi.event.vinnsla.EventStatus;
import hi.event.vinnsla.EventStorage;
import hi.event.vinnsla.EventValidator;
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
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
            ""                          // description
    ); // New Event by default


    private boolean isNewEvent = true;

    private EventManagerController controller;
    private EventStorage eventStorage = new EventStorage();
    private EventValidator validator = new EventValidator();

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

    public void initialize() {
        // Initialize ComboBoxes
        fxGroup.setItems(FXCollections.observableArrayList(Group.values()));
        fxStatus.setItems(FXCollections.observableArrayList(EventStatus.values()));
        fxStatus.getSelectionModel().select(EventStatus.ACTIVE); // Set default status as ACTIVE

        // Bind properties from the event object
        fxTitle.textProperty().bindBidirectional(event.titleProperty());
        fxGroup.valueProperty().bindBidirectional(event.groupProperty());
        fxDate.valueProperty().bindBidirectional(event.dateProperty());
        fxTime.valueProperty().addListener((obs, oldVal, newVal) -> event.timeProperty().set(LocalTime.of(newVal, 0))); // Set event time
        fxDescription.textProperty().bindBidirectional(event.descriptionProperty()); // Bind description field

        fxTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour())); // Default to current hour

        // Set up the time spinner
        setUpTimeSpinner();

        // Initialize file choosers
        initializeFileChoosers();

        // If this is an existing event (edit), populate the form
        if (!isNewEvent) {
            populateFormWithEventDetails();
        }
    }

    private void initializeFileChoosers() {
        // Image file chooser filter
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));
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

    private void populateFormWithEventDetails() {
        // Use JavaFX properties for binding UI elements
        fxTitle.textProperty().bindBidirectional(event.titleProperty()); // Using titleProperty() to bind
        fxGroup.valueProperty().bindBidirectional(event.groupProperty()); // Using groupProperty() to bind
        fxDate.valueProperty().bindBidirectional(event.dateProperty()); // Using dateProperty() to bind
        fxTime.valueProperty().addListener((obs, oldVal, newVal) ->
                event.timeProperty().set(LocalTime.of(newVal, 0))); // Binding time
        fxDescription.textProperty().bindBidirectional(event.descriptionProperty()); // Binding descriptionProperty()

        // Bind the status ComboBox using the statusProperty()
        fxStatus.getSelectionModel().select(event.statusProperty().get());

        // Set the media image and video if they exist
        if (event.imageMediaProperty().get() != null) {
            fxImage.setImage(event.imageMediaProperty().get()); // Using imageMediaProperty()
        }

        if (event.videoMediaProperty().get() != null) {
            Media media = event.videoMediaProperty().get(); // Using videoMediaProperty()
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            fxMediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        }
    }


    @FXML
    void openImage(ActionEvent actionEvent) {
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));
        File file = fxFileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                // Get the selected file path
                Path selectedFile = file.toPath();
                String relativeImagePath = "media/" + selectedFile.getFileName().toString();

                // Create an Image from the file
                Image image = new Image(file.toURI().toString());

                // Set the image to the UI element
                fxImage.setImage(image);

                // Update the event's imageMedia property (JavaFX Property)
                event.imageMediaProperty().set(image); // Set JavaFX property value

                // Update the plain value of imageMediaPath (String value)
                event.imageMediaPathProperty().set(relativeImagePath);

            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        } else {
            showError("No image selected.");
        }
    }


    @FXML
    void openMedia(ActionEvent actionEvent) {
        fxFileChooser.getExtensionFilters().clear();
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv", "*.mov"));
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.flac"));

        File file = fxFileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                Path selectedFile = file.toPath();
                String relativeMediaPath = "media/" + selectedFile.getFileName().toString();

                Media media = new Media(file.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                fxMediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();

                this.event.videoMediaProperty().set(media);
                this.event.videoMediaPathProperty().set(relativeMediaPath);
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        } else {
            showError("No media selected.");
        }
    }


    @FXML
    void saveEvent(ActionEvent actionEvent) {
        if (this.event != null) {
            // Extract the plain values from JavaFX properties before saving
            event.extractToPlainValues();  // Extract plain values into the fields (like titleValue, dateValue, etc.)

            // Validate event using the EventValidator
            boolean isEventValid = EventValidator.isValid(event);

            // If there are validation errors, show an alert and return
            if (!isEventValid) {
                showError("Error: \nOne or more event fields are invalid.");
                return;  // If validation fails, stop the process
            }

            // If no validation errors, proceed with saving
            try {
                if (isNewEvent) {
                    // Save the new event using EventStorage (JSON)
                    EventStorage.saveEvents(List.of(this.event)); // Save the event with the plain fields
                    System.out.println("Event saved: " + event.getTitleValue()); // Access the plain field using getter
                    controller.addEvent(this.event);
                } else {
                    // Save the updated event using EventStorage
                    EventStorage.saveEvents(List.of(this.event)); // Save the event with the plain fields
                    System.out.println("Event updated: " + event.getTitleValue());
                    controller.updateEvent(this.event);
                }

                // Show success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event Saved");
                alert.setHeaderText("Event saved successfully!");
                alert.setContentText("Your event has been saved.");
                alert.showAndWait();

                // Re-enable the buttons in EventManagerController
                if (controller != null) {
                    controller.enableButtons();  // Call the method to re-enable the buttons
                }

                // Close the window after saving
                closeWindowOnSave();

            } catch (IOException e) {
                showError("Error saving event: " + e.getMessage());
            }
        } else {
            System.out.println("No event to save.");
        }
    }




    // Helper method to show an error alert
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void closeWindowOnSave() {
        Stage stage = (Stage) fxSaveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleCancel(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Event");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("All changes will be discarded.");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                closeWindow();
            }
        });
    }

    private void closeWindow() {
        Stage stage = (Stage) fxCancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public String toString() {
        return "NewEventController{" +
                "fxTitle=" + fxTitle.getText() +
                "} " + super.toString();
    }


    public Event getEvent() {
        return event;
    }
}
