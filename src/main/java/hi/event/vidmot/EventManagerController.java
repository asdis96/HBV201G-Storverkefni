package hi.event.vidmot;


import hi.event.vinnsla.Event;
import hi.event.vinnsla.EventStatus;
import hi.event.vinnsla.EventStorage;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class EventManagerController implements Initializable {

    private ObservableList<Event> events = FXCollections.observableArrayList();
    private EventStorage eventStorage = new EventStorage(); // Assuming EventStorage is a class for storage management

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

    @FXML
    private Button btnProcessSelected;

    // Get the list of events (used in NewEventController for saving events)
    public ObservableList<Event> getEvents() {
        return events;
    }

    private NewEventController currentView;  // Currently viewed event form

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load events from storage
        events = EventStorage.loadEvents();
        eventTableView.setItems(events);

        // Automatically save events when the list changes
        events.addListener((ListChangeListener<Event>) change -> {
            // Save events when changes occur
            EventStorage.saveEvents(events);
        });

        // Other initializations (columns, buttons, etc.)
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Ensure that btnProcessSelected is not null before setting its action
        if (btnProcessSelected != null) {
            btnProcessSelected.setOnAction(event -> processSelectedEvents());
        } else {
            System.out.println("Error: btnProcessSelected is null!");
        }

        // Setting selection mode for eventTableView
        eventTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }


    public void toggleSidebarVisibility() {
        // This method will toggle the visibility of the sidebar
        if (sidebarContainer.isVisible()) {
            sidebarContainer.setVisible(false);
        } else {
            sidebarContainer.setVisible(true);
        }
    }


    // This method will be called when the button is clicked to process selected events
    public void processSelectedEvents() {
        getSelectedEvents();
    }

    public void getSelectedEvents() {
        for (Event event : eventTableView.getItems()) {
            if (event.isSelected()) {  // Using the getter method for selected (isSelected())
                System.out.println("Selected event: " + event.getTitle());
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
            // Ask the user for confirmation before deleting the event
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this event?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                // Remove the event from the internal list and the TableView
                events.remove(selectedEvent);
                eventTableView.setItems(events); // Update the TableView
                EventStorage.saveEvents(events); // Save the updated events list
            }
        } else {
            // Alert if no event is selected
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an event to delete.", ButtonType.OK);
            alert.showAndWait();
        }
    }



    @FXML
    void onChangeStatus(ActionEvent actionEvent) {
        // Get the selected event from the TableView
        Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            // Toggle status between ACTIVE and INACTIVE
            EventStatus currentStatus = selectedEvent.getStatus();
            EventStatus newStatus = (currentStatus == EventStatus.ACTIVE) ? EventStatus.INACTIVE : EventStatus.ACTIVE;
            selectedEvent.setStatus(newStatus);

            // Update the table view to reflect the status change
            eventTableView.refresh();
        } else {
            // Alert if no event is selected
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an event to change its status.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void onLogout(ActionEvent actionEvent) {
        // Perform logout functionality (e.g., clear session data, return to login screen)
        Alert logoutAlert = new Alert(Alert.AlertType.INFORMATION, "You have successfully logged out.", ButtonType.OK);
        logoutAlert.showAndWait();

        // Close the application or navigate to the login screen
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close(); // Close the current window (logout)
    }

    // Add an Event to the list
    public void addEvent(Event event) {
        if (event != null) {
            events.add(event);             // Add the event to the observable list
            eventTableView.setItems(events); // Refresh the table view
            EventStorage.saveEvents(events); // Save the updated list to storage
        }
    }

    // Update an existing Event
    public void updateEvent(Event updatedEvent) {
        if (updatedEvent != null) {
            for (int i = 0; i < events.size(); i++) {
                Event existingEvent = events.get(i);
                if (existingEvent.equals(updatedEvent)) {
                    events.set(i, updatedEvent); // Update the event in the list
                    eventTableView.setItems(events); // Refresh the table view
                    break;
                }
            }
            EventStorage.saveEvents(events); // Save the updated list to storage
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
