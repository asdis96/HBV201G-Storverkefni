package hi.event.vidmot;


import hi.event.vinnsla.Event;
import hi.event.vinnsla.EventStatus;
import hi.event.vinnsla.EventStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EventManagerController implements Initializable {
    @FXML
    public VBox sidebarContainer;
    @FXML
    public BorderPane root;
    @FXML
    private Button fxNewEvent;
    @FXML
    private Button fxViewEvent;
    @FXML
    private Button fxEditEvent;
    @FXML
    private Button fxDeleteEvent;
    @FXML
    private Button fxChangeStatus;
    @FXML
    private Button fxLogout;
    @FXML
    private StackPane fxEventViews;
    @FXML
    private TableView<Event> eventTableView;
    @FXML
    private TableColumn<Event, Boolean> selectColumn;
    @FXML
    private TableColumn<Event, String> titleColumn;
    @FXML
    private TableColumn<Event, String> dateColumn;
    @FXML
    private TableColumn<Event, String> groupColumn;
    @FXML
    private TableColumn<Event, String> statusColumn;

    // Instance variables to store events and handle storage
    private List<Event> events = new ArrayList<>();  // List to store all events
    private EventStorage eventStorage = new EventStorage();  // EventStorage instance to handle saving/loading

    @FXML
    private Button btnProcessSelected;

    private ObservableList<Event> observableEvents = FXCollections.observableArrayList();
    private NewEventController currentView;  // Currently viewed event form

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initializing the columns in the TableView
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

       loadEventsFromStorage();

        // Ensure that btnProcessSelected is not null before setting its action
        if (btnProcessSelected != null) {
            btnProcessSelected.setOnAction(event -> processSelectedEvents());
        } else {
            System.out.println("Error: btnProcessSelected is null!");
        }

        // Setting selection mode for eventTableView
        eventTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void loadEventsFromStorage() {
        try {
            // Load events from storage
            events = EventStorage.loadEvents();  // Assuming loadEvents returns a List<Event>
            if (events != null) {
                // Populate the observable list
                observableEvents = FXCollections.observableArrayList(events);
                // Ensure the JavaFX properties are populated from plain values
                for (Event event : observableEvents) {
                    event.populateFromPlainValues();  // Populate JavaFX properties from plain values
                }
                // Bind the observable list to the TableView
                eventTableView.setItems(observableEvents);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load events from file.", ButtonType.OK);
            alert.showAndWait();
        }
    }


    public void enableButtons() {
        fxNewEvent.setDisable(false);
        fxEditEvent.setDisable(false);
        fxDeleteEvent.setDisable(false);
        fxChangeStatus.setDisable(false);
    }



    // This method will be called when the button is clicked to process selected events
    public void processSelectedEvents() {
        getSelectedEvents();
    }

    // Method to get and print selected events
    public void getSelectedEvents() {
        for (Event event : eventTableView.getItems()) {
            if (event.selectedProperty().get()) {  // Use the JavaFX property to get the selected state
                System.out.println("Selected event: " + event.titleProperty().get());  // Access title through JavaFX property
            }
        }
    }


    @FXML
    void onNewEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("new-event.fxml"));
            Parent root = loader.load(); // Let FXMLLoader create and initialize the controller
            NewEventController newEventController = loader.getController(); // Get the controller created by FXML
            newEventController.setController(this); // Pass the EventManagerController if needed

            Stage newEventStage = new Stage();
            newEventStage.setTitle("Create New Event");
            newEventStage.setScene(new Scene(root));
            newEventStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onViewEvent() {
        Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            EventView eventView = new EventView(selectedEvent, eventTableView); // or fxEventViews
            eventView.initOwner(eventTableView.getScene().getWindow());
            eventView.showAndWait();
        }
    }


    @FXML
    void onEditEvent(ActionEvent event) {
        Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("new-event.fxml"));
                Parent root = loader.load();

                NewEventController newEventController = loader.getController();
                newEventController.setEditMode(this, selectedEvent);

                Stage editStage = new Stage();
                editStage.setTitle("Edit Event");
                editStage.setScene(new Scene(root));
                editStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an event to edit.", ButtonType.OK);
            alert.showAndWait();
        }
    }


    @FXML
    void onDeleteEvent(ActionEvent actionEvent) {
        Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            // Confirmation before deletion
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this event?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                // Remove the event from both the internal list and the observable list
                events.remove(selectedEvent);
                observableEvents.remove(selectedEvent);

                // Rebind the observable list to the table view
                eventTableView.setItems(observableEvents);

                // Save the updated list of events to storage
                saveEventsToStorage();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an event to delete.", ButtonType.OK);
            alert.showAndWait();
        }
    }




    @FXML
    void onChangeStatus(ActionEvent actionEvent) {
        // Get the selected event from the TableView
        Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            // Access the current status using the JavaFX property
            EventStatus currentStatus = selectedEvent.statusProperty().get();
            // Toggle status between ACTIVE and INACTIVE
            EventStatus newStatus = (currentStatus == EventStatus.ACTIVE) ? EventStatus.INACTIVE : EventStatus.ACTIVE;
            // Update the status using JavaFX property and plain value
            selectedEvent.statusProperty().set(newStatus);
            selectedEvent.setStatusValue(newStatus);  // Update the plain value

            // Refresh the table view to reflect the status change
            eventTableView.refresh();

            // Save the updated events to storage
            saveEventsToStorage();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an event to change its status.", ButtonType.OK);
            alert.showAndWait();
        }
    }




    public void saveEventsToStorage() {
        try {
            // Convert JavaFX properties to plain values before saving
            for (Event event : events) {
                event.extractToPlainValues();  // Extract to plain values for serialization
            }
            // Save the list of events to storage
            EventStorage.saveEvents(events);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save events.", ButtonType.OK);
            alert.showAndWait();
        }
    }




    // Logout and save events
    @FXML
    public void onLogout(ActionEvent actionEvent) {
        // Handle logout
        saveEventsToStorage();
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }



    public void addEvent(Event event) {
        if (event != null) {
            // Add the event to both internal lists
            events.add(event);
            observableEvents.add(event);

            // Rebind the TableView with the updated list
            eventTableView.setItems(observableEvents);  // Update TableView with new event

            // Save the events list after adding
            saveEventsToStorage();
        }
    }

    public void updateEvent(Event updatedEvent) {
        if (updatedEvent != null) {
            for (int i = 0; i < events.size(); i++) {
                Event existingEvent = events.get(i);
                if (existingEvent.equals(updatedEvent)) {
                    // Update internal lists with the updated event
                    events.set(i, updatedEvent);
                    observableEvents.set(i, updatedEvent);
                    eventTableView.setItems(observableEvents);  // Rebind TableView

                    // Save the updated events to storage
                    saveEventsToStorage();
                    break;
                }
            }
        }
    }





    /**
     * Switch the view to display the selected event
     * @param targetView The NewEventController view to be displayed
     */
    private void switchView(Node targetView) {
        for (Node node : fxEventViews.getChildren()) {
            node.setVisible(false); // Hide all event views
        }
        targetView.setVisible(true); // Show the target event view
    }

    /**
     * Finds the NewEventController view associated with a specific event
     * @param event The event to search for
     * @return The NewEventController view associated with the event
     */
    private Optional<NewEventController> findViewForEvent(Event event) {
        for (Node node : fxEventViews.getChildren()) {
            if (((NewEventController) node).getEvent().equals(event)) {
                return Optional.of((NewEventController) node);
            }
        }
        return Optional.empty();
    }




    /**
     * Finnur viðburð samkvæmt heiti viðburðar
     * @param heiti heiti viðburðar
     * @return viðburðurinn
     */
    private Optional<Event> finnaVidburd(String heiti) {
        for (Event event : events) {
            if (event.titleProperty().get().equals(heiti)) {
                return Optional.of(event);
            }
        }
        return Optional.empty();
    }

    /**
     * Ekki hluti af verkefni en sýnt hér til fróðleiks
     *
     * @param source
     */
    public void breytaLit(RadioMenuItem source) {
        System.out.println(source.getText());
        // athuga
        if (source.getText().equals("Svartur")) {
            fxEventViews.getStyleClass().remove("hvitur");
            fxEventViews.getStyleClass().remove("svartLetur");
            fxEventViews.getStyleClass().add(source.getText().toLowerCase());
            fxEventViews.getStyleClass().add("hvittLetur");
        }
        else if (source.getText().equals("Hvítur")) {
            fxEventViews.getStyleClass().remove("svartur");
            fxEventViews.getStyleClass().remove("hvittLetur");
            fxEventViews.getStyleClass().add("svartLetur");
            fxEventViews.getStyleClass().add("hvitur");
        }
    }

}
