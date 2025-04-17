package hi.event.vidmot;


import hi.event.vinnsla.Event;
import hi.event.vinnsla.EventStatus;
import hi.event.vinnsla.EventStorage;
import hi.event.vinnsla.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private ComboBox<String> sortComboBox;
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
    private TextField searchField;
    @FXML
    private Button btnProcessSelected;

    private List<Event> events = new ArrayList<>();
    private EventStorage eventStorage = new EventStorage();
    private ObservableList<Event> observableEvents = FXCollections.observableArrayList();
    private NewEventController currentView;


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        initializeTableColumns();
        loadEventsFromStorage();
        setupSearchFilter();
        setupSortComboBox();
        setupProcessButton();
        eventTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void initializeTableColumns() {
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        dateColumn.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            return new SimpleStringProperty(event.getFormattedDateForFXML());
        });
        dateColumn.setCellFactory(column -> new TableCell<Event, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupSortComboBox() {
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.equals("Sort by A–Z")) {
                    titleColumn.setSortType(TableColumn.SortType.ASCENDING);
                } else if (newVal.equals("Sort by Z–A")) {
                    titleColumn.setSortType(TableColumn.SortType.DESCENDING);
                }
                eventTableView.getSortOrder().clear();
                eventTableView.getSortOrder().add(titleColumn);
            }
        });

        sortComboBox.getSelectionModel().select("Sort by A–Z");
    }

    private void setupProcessButton() {
        if (btnProcessSelected != null) {
            btnProcessSelected.setOnAction(event -> processSelectedEvents());
        } else {
            System.out.println("Error: btnProcessSelected is null!");
        }
    }

    private void setupSearchFilter() {
        FilteredList<Event> filteredEvents = new FilteredList<>(observableEvents, e -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEvents.setPredicate(event -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return event.getTitleValue().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        SortedList<Event> sortedEvents = new SortedList<>(filteredEvents);
        sortedEvents.comparatorProperty().bind(eventTableView.comparatorProperty());
        eventTableView.setItems(sortedEvents);
        eventTableView.getSortOrder().add(titleColumn);
    }


    private void loadEventsFromStorage() {
        try {
            events = EventStorage.loadEvents();
            if (events != null) {
                observableEvents = FXCollections.observableArrayList(events);
                for (Event event : observableEvents) {
                    event.populateFromPlainValues();
                }
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

    public void processSelectedEvents() {
        getSelectedEvents();
    }

    public void getSelectedEvents() {
        for (Event event : eventTableView.getItems()) {
            if (event.selectedProperty().get()) {
                System.out.println("Selected event: " + event.titleProperty().get());
            }
        }
    }

    @FXML
    void onNewEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("new-event.fxml"));
            Parent root = loader.load();
            NewEventController newEventController = loader.getController();
            newEventController.setController(this);

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
            EventView eventView = new EventView(selectedEvent, eventTableView);
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
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load event details for editing.", ButtonType.OK);
                // Apply the current theme to the alert
                EventManagerApplication.applyStylesheetToAlert(alert);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an event to edit.", ButtonType.OK);
            // Apply the current theme to the alert
            EventManagerApplication.applyStylesheetToAlert(alert);
            alert.showAndWait();
        }
    }


    @FXML
    void onDeleteEvent(ActionEvent actionEvent) {
        Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this event?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {

                events.remove(selectedEvent);
                observableEvents.remove(selectedEvent);

                eventTableView.setItems(observableEvents);

                saveEventsToStorage();
            }
            // Apply the current theme to the alert
            EventManagerApplication.applyStylesheetToAlert(confirmationAlert);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an event to delete.", ButtonType.OK);
            // Apply the current theme to the alert
            EventManagerApplication.applyStylesheetToAlert(alert);
            alert.showAndWait();
        }
    }


    @FXML
    void onChangeStatus(ActionEvent actionEvent) {
        Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            EventStatus currentStatus = selectedEvent.statusProperty().get();
            EventStatus newStatus;

            switch (currentStatus) {
                case ACTIVE:
                    newStatus = EventStatus.INACTIVE;
                    break;
                case INACTIVE:
                    newStatus = EventStatus.CANCELLED;
                    break;
                case CANCELLED:
                default:
                    newStatus = EventStatus.ACTIVE;
                    break;
            }

            selectedEvent.statusProperty().set(newStatus);
            selectedEvent.setStatusValue(newStatus);
            eventTableView.refresh();
            saveEventsToStorage();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an event to change its status.", ButtonType.OK);
            // Apply the current theme to the alert
            EventManagerApplication.applyStylesheetToAlert(alert);
            alert.showAndWait();
        }
    }



    public void saveEventsToStorage() {
        try {
            for (Event event : events) {
                event.extractToPlainValues();
            }

            EventStorage.saveEvents(events);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save events.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void addEvent(Event event) {
        if (event != null) {
            events.add(event);
            observableEvents.add(event);
            eventTableView.setItems(observableEvents);
            saveEventsToStorage();
        }
    }

    public void updateEvent(Event updatedEvent) {
        if (updatedEvent != null) {
            for (int i = 0; i < events.size(); i++) {
                Event existingEvent = events.get(i);
                if (existingEvent.equals(updatedEvent)) {
                    events.set(i, updatedEvent);
                    observableEvents.set(i, updatedEvent);
                    eventTableView.setItems(observableEvents);
                    saveEventsToStorage();
                    break;
                }
            }
        }
    }

    public void changeTheme(RadioMenuItem selectedThemeItem) {
        // Remove old stylesheets
        root.getStylesheets().clear();

        // Add new stylesheet based on selection
        if (selectedThemeItem.getId().equals("fxDarkMode")) {
            root.getStylesheets().add(getClass().getResource("/css/dark-mode.css").toExternalForm());
        } else if (selectedThemeItem.getId().equals("fxLightMode")) {
            root.getStylesheets().add(getClass().getResource("/css/light-mode.css").toExternalForm());
        }
    }

    public void breytaLit(RadioMenuItem source) {
        System.out.println(source.getText());
        // athuga
        if (source.getText().equals("Dark Mode")) {
            fxEventViews.getStyleClass().remove("");
            fxEventViews.getStyleClass().remove("");
            fxEventViews.getStyleClass().add(source.getText().toLowerCase());
            fxEventViews.getStyleClass().add("");
        } else if (source.getText().equals("Light Mode")) {
            fxEventViews.getStyleClass().remove("");
            fxEventViews.getStyleClass().remove("");
            fxEventViews.getStyleClass().add("");
            fxEventViews.getStyleClass().add("");


        }
    }
}
