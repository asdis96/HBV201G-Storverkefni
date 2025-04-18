package hi.event.vidmot;

import hi.event.vinnsla.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 * <p>
 *  Description  : Controller for the New Event view in the Event Manager application.
 *  This controller manages the user interface for creating or editing an event,
 *  handling input fields for event details, image/media file uploads, and saving the event.
 * <p>
 *  It supports both creating new events and editing existing ones. This controller
 *  interacts with the `EventManagerController` and `EventStorage` classes to
 *  save the event data and manage the event lifecycle.
 *
 *
 *****************************************************************************/

public class NewEventController extends VBox {
    @FXML
    private HBox fxMainView;
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
    private Button fxCancelButton;
    @FXML
    private TextArea fxDescription;
    @FXML
    private Button fxSaveButton;
    @FXML
    private FileChooser fxFileChooser = new FileChooser();
    @FXML
    private VBox mediaView;
    @FXML
    private Spinner<Integer> fxHoursSpinner;
    @FXML
    private Spinner<Integer> fxMinutesSpinner;

    @FXML
    private Event event = new Event(
            false,
            "",
            LocalDate.now(),
            Group.ENTERTAINMENT,
            EventStatus.ACTIVE,
            null,
            null,
            LocalTime.now(),
            ""
    );

    private MediaController mediaController;
    private EventManagerController controller;
    private EventStorage eventStorage = new EventStorage();
    private EventValidator validator = new EventValidator();
    private boolean isNewEvent = true;

    /**
     * Sets the controller to edit an existing event.
     *
     * @param controller The event manager controller to interact with.
     * @param event The event to be edited.
     */
    public void setEditMode(EventManagerController controller, Event event) {
        this.controller = controller;
        this.event = event;
        this.isNewEvent = false;

        populateFormWithEventDetails();
    }

    /**
     * Sets the controller to create a new event.
     *
     * @param controller The event manager controller to interact with.
     */
    public void setController(EventManagerController controller) {
        this.controller = controller;
        this.isNewEvent = true;
        initialize();
    }

    /**
     * Initializes the form with default values and bindings for new event creation.
     */
    public void initialize() {
        fxGroup.setItems(FXCollections.observableArrayList(Group.values()));
        fxStatus.setItems(FXCollections.observableArrayList(EventStatus.values()));
        fxStatus.getSelectionModel().select(EventStatus.ACTIVE);
        fxTitle.textProperty().bindBidirectional(event.titleProperty());
        fxGroup.valueProperty().bindBidirectional(event.groupProperty());
        fxDate.valueProperty().bindBidirectional(event.dateProperty());
        formatDatePicker(fxDate);
        fxDescription.textProperty().bindBidirectional(event.descriptionProperty());
        setUpTimeSpinner();
        initializeFileChoosers();

        if (!isNewEvent) {
            populateFormWithEventDetails();
        }

        Platform.runLater(() -> {
            Scene scene = fxMainView.getScene();
            if (scene != null) {
                String currentTheme = EventManagerApplication.getTheme();
                scene.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());
            }
        });
    }

    /**
     * Sets up the time spinner for selecting the event time.
     */
    private void setUpTimeSpinner() {
        fxHoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour()));
        fxMinutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute()));
        fxHoursSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateEventTime());
        fxMinutesSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateEventTime());
        event.timeProperty().addListener((obs, oldTime, newTime) -> updateSpinners(newTime));
        updateSpinners(LocalTime.now());
    }

    /**
     * Updates the event time based on the selected hour and minute.
     */
    private void updateEventTime() {
        LocalTime selectedTime = LocalTime.of(fxHoursSpinner.getValue(), fxMinutesSpinner.getValue());
        event.timeProperty().set(selectedTime);
    }

    /**
     * Updates the time spinners based on the current event time.
     *
     * @param time The current event time.
     */
    private void updateSpinners(LocalTime time) {
        fxHoursSpinner.getValueFactory().setValue(time.getHour());
        fxMinutesSpinner.getValueFactory().setValue(time.getMinute());
    }

    /**
     * Initializes the file chooser for selecting image and media files.
     */
    private void initializeFileChoosers() {
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));
    }

    /**
     * Formats the DatePicker control to display the date in the format dd/MM/yyyy.
     *
     * @param datePicker The DatePicker to format.
     */
    private void formatDatePicker(DatePicker datePicker) {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        });
    }

    /**
     * Populates the form with the details of an existing event.
     */
    private void populateFormWithEventDetails() {
        fxTitle.textProperty().bindBidirectional(event.titleProperty());
        fxGroup.valueProperty().bindBidirectional(event.groupProperty());
        fxDate.valueProperty().bindBidirectional(event.dateProperty());
        formatDatePicker(fxDate);
        LocalTime eventTime = event.timeProperty().get();
        fxHoursSpinner.getValueFactory().setValue(eventTime.getHour());
        fxMinutesSpinner.getValueFactory().setValue(eventTime.getMinute());
        fxDescription.textProperty().bindBidirectional(event.descriptionProperty());
        fxStatus.getSelectionModel().select(event.statusProperty().get());

        String imagePath = event.imageMediaPathProperty().get();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    Image image = new Image(imageUrl.toExternalForm());
                    fxImage.setImage(image);
                    event.imageMediaProperty().set(image);
                } else {
                    System.out.println("Image not found: " + imagePath);
                }
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
            }
        }


        String videoPath = event.videoMediaPathProperty().get();
        if (videoPath != null && !videoPath.isEmpty()) {
            try {
                URL mediaUrl = getClass().getResource(videoPath);
                if (mediaUrl != null) {
                    Media media = new Media(mediaUrl.toExternalForm());
                    loadMediaViewController(media);
                } else {
                    System.out.println("Video not found: " + videoPath);
                }
            } catch (Exception e) {
                System.out.println("Error loading video: " + e.getMessage());
            }
        }

    }


    /**
     * Opens the file chooser to select an image and updates the event with the selected image.
     *
     * @param actionEvent The action event triggered when the image button is clicked.
     */
    @FXML
    void openImage(ActionEvent actionEvent) {
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));
        File file = fxFileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                Path selectedFile = file.toPath();
                String relativeImagePath = "media/" + selectedFile.getFileName().toString();
                Image image = new Image(file.toURI().toString());
                fxImage.setImage(image);
                event.imageMediaProperty().set(image);
                event.imageMediaPathProperty().set(relativeImagePath);
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        } else {
            showError("No image selected.");
        }
    }

    /**
     * Opens the file chooser to select a video or audio file and updates the event with the selected media.
     *
     * @param actionEvent The action event triggered when the media button is clicked.
     */
    @FXML
    void openMedia(ActionEvent actionEvent) {
        FileChooser fxFileChooser = new FileChooser();
        fxFileChooser.getExtensionFilters().clear();
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv", "*.mov"));
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.flac"));

        File file = fxFileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                Path selectedFile = file.toPath();
                String relativeMediaPath = "media/" + selectedFile.getFileName().toString();
                event.videoMediaPathProperty().set(relativeMediaPath);

                Media media = new Media(file.toURI().toString());
                loadMediaViewController(media);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showError("No media file selected.");
        }
    }

    /**
     * Loads the media into the media view controller for playback.
     *
     * @param media The media to load into the view.
     */
    private void loadMediaViewController(Media media) {
        FXMLLoader mediaLoader = new FXMLLoader(getClass().getResource("media-view.fxml"));
        mediaLoader.setControllerFactory(param -> {
            mediaController = new MediaController();
            return mediaController;
        });

        try {
            VBox mediaVBox = mediaLoader.load();
            mediaView.getChildren().setAll(mediaVBox.getChildren());
            mediaController.setMediaPlayer(media);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Saves the event to storage and updates the event manager with the new or edited event.
     *
     * @param actionEvent The action event triggered when the save button is clicked.
     */
    @FXML
    void saveEvent(ActionEvent actionEvent) {
        if (this.event != null) {
            event.extractToPlainValues();
            boolean isEventValid = EventValidator.isValid(event);
            if (!isEventValid) {
                showError("Error: \nOne or more event fields are invalid.");
                return;
            }

            try {
                if (isNewEvent) {
                    EventStorage.saveEvents(List.of(this.event));
                    System.out.println("Event saved: " + event.getTitleValue());
                    controller.addEvent(this.event);
                } else {
                    EventStorage.saveEvents(List.of(this.event));
                    System.out.println("Event updated: " + event.getTitleValue());
                    controller.updateEvent(this.event);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event Saved");
                alert.setHeaderText("Event saved successfully!");
                alert.setContentText("Your event has been saved.");
                EventManagerApplication.applyStylesheetToAlert(alert);
                alert.showAndWait();

                if (controller != null) {
                    controller.enableButtons();
                }
                closeWindowOnSave();

            } catch (IOException e) {
                showError("Error saving event: " + e.getMessage());
            }
        } else {
            System.out.println("No event to save.");
        }
    }

    /**
     * Shows an error alert with the provided message.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);
        EventManagerApplication.applyStylesheetToAlert(alert);
        alert.showAndWait();
    }

    /**
     * Closes the current window after saving.
     */
    private void closeWindowOnSave() {
        Stage stage = (Stage) fxSaveButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the cancel button click. Shows confirmation before closing without saving.
     *
     * @param actionEvent The action event triggered when cancel is clicked.
     */
    @FXML
    void handleCancel(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Event");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("All changes will be discarded.");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        EventManagerApplication.applyStylesheetToAlert(alert);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                closeWindow();
            }
        });
    }

    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) fxCancelButton.getScene().getWindow();
        stage.close();
    }


    /**
     * Returns a string representation of the controller, useful for debugging.
     *
     * @return A string representing the controller and title.
     */
    @Override
    public String toString() {
        return "NewEventController{" +
                "fxTitle=" + fxTitle.getText() +
                "} " + super.toString();
    }

    /**
     * Returns the current event instance managed by the controller.
     *
     * @return The current event.
     */
    public Event getEvent() {
        return event;
    }
}
