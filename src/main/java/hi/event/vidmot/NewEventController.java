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
 *
 *  Description  :
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

    public void setEditMode(EventManagerController controller, Event event) {
        this.controller = controller;
        this.event = event;
        this.isNewEvent = false;

        populateFormWithEventDetails();
    }

    public void setController(EventManagerController controller) {
        this.controller = controller;
        this.isNewEvent = true;
        initialize();
    }

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

        // Delay stylesheet application until scene is available
        Platform.runLater(() -> {
            Scene scene = fxMainView.getScene();
            if (scene != null) {
                String currentTheme = EventManagerApplication.getTheme();
                scene.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());
            }
        });
    }

    private void setUpTimeSpinner() {
        // Set up the hour spinner with a range from 0 to 23 and default to current hour
        fxHoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour()));

        // Set up the minute spinner with a range from 0 to 59 and default to current minute
        fxMinutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute()));

        // Listener for the hour spinner - when it changes, update the event time
        fxHoursSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateEventTime());

        // Listener for the minute spinner - when it changes, update the event time
        fxMinutesSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateEventTime());

        // Listener for changes in event's time - update both spinners when time changes
        event.timeProperty().addListener((obs, oldTime, newTime) -> updateSpinners(newTime));

        // Set initial values for the spinners based on event time
        updateSpinners(LocalTime.now());
    }

    private void updateEventTime() {
        // Update the event's time based on the selected hour and minute
        LocalTime selectedTime = LocalTime.of(fxHoursSpinner.getValue(), fxMinutesSpinner.getValue());
        event.timeProperty().set(selectedTime);
    }

    private void updateSpinners(LocalTime time) {
        // Set the spinner values based on the event's time
        fxHoursSpinner.getValueFactory().setValue(time.getHour());
        fxMinutesSpinner.getValueFactory().setValue(time.getMinute());
    }



    private void initializeFileChoosers() {
        fxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));
    }

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

                Media media = new Media(file.toURI().toString());

                loadMediaViewController(media);

            } catch (Exception e) {
                e.printStackTrace();  // Handle error
            }
        }
    }

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
