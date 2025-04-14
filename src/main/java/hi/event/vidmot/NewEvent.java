package hi.event.vidmot;
import hi.event.vinnsla.Event;
import hi.event.vinnsla.EventStatus;
import hi.event.vinnsla.Group;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

/******************************************************************************
 *  Nafn    : Ásdís Stefánsdóttir
 *  T-póstur: ahl4@hi.is
 *
 *  Lýsing  :
 *****************************************************************************/
public class NewEvent extends VBox {

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
    private TextArea fxDescription; // Added description field


    @FXML
    private FileChooser fxFileChooser;

    @FXML
    private Button fxSaveButton;

    @FXML
    private Event event = new Event(false, "", LocalDate.now(), Group.ENTERTAINMENT, EventStatus.ACTIVE, null, null, LocalTime.now(), ""); // New Event by default

    private boolean isNewEvent = true;
    private EventManagerController controller;

    // Default constructor for creating a new event
    public NewEvent(EventManagerController controller) {
        this();
        this.controller = controller;
        isNewEvent = true;
        initialize(); // Initialize for new event
    }

    // Constructor for editing an existing event
    public NewEvent(EventManagerController controller, Event event) {
        this(controller); // Call the default constructor
        this.event = event; // Use the existing event
        isNewEvent = false; // It's an edited event, not new
        initialize(); // Initialize for editing the event
    }

    // Load the FXML and initialize the view
    public NewEvent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("new-event.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        try {
            fxmlLoader.load(); // Load the FXML file for the layout
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

        // Set the event status
        fxStatus.getSelectionModel().select(event.getStatus());
    }

    // Event handler for opening the image file chooser
    @FXML
    void openImage(ActionEvent event) {
        File file = fxFileChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            this.event.setImageMedia(image);
        } else {
            System.out.println("No image selected");
        }
    }

    // Event handler for opening the media file chooser and setting the media
    @FXML
    void openMedia(ActionEvent event) {
        File file = fxFileChooser.showOpenDialog(null);
        if (file != null) {
            Media media = new Media(file.toURI().toString());
            this.event.setVideoMedia(media);
        } else {
            System.out.println("No file selected for media");
        }
    }

    // Save the event (either new or edited)
    @FXML
    void saveEvent(ActionEvent event) {
        if (this.event != null) {
            if (isNewEvent) {
                // If it's a new event, add it to the list
                controller.addEvent(this.event);
            } else {
                // If it's an edited event, update the existing event in the list
                controller.updateEvent(this.event);
            }
            System.out.println("Event saved: " + this.event.getTitle());
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


    // Helper method to view the component
    @Override
    public String toString() {
        return "NewEvent{" +
                ", fxHeiti=" + fxTitle.getText() +
                "} " + super.toString();
    }

    // Get the created Event object
    public Event getEvent() {
        return event;
    }
}
