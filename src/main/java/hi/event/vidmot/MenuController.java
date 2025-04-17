package hi.event.vidmot;

import hi.event.vinnsla.User;
import hi.event.vinnsla.UserSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 *
 *  Description  :
 *
 *
 *****************************************************************************/

public class MenuController {

    @FXML
    public RadioMenuItem fxLightMode;
    @FXML
    public RadioMenuItem fxDarkMode;
    public ToggleGroup theme;

    /**
     * Quits the program
     * @param event unused
     */
    @FXML
    void onExit(ActionEvent event) {
        EventManagerController controller = getEventManagerController();
        if (controller != null) {
            controller.saveEventsToStorage();
            Platform.exit();
        } else {
            System.out.println("Error: EventManagerController is null!");
        }
    }


    /**
     * Gives information about the program
     * @param event unused
     */
    @FXML
    void onAbout(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.INFORMATION,
                "Author: Ásdís Stefánsdíttr, version 1.0",
                ButtonType.OK);
        a.setTitle("Event manager");
        a.setHeaderText("EventManager");
        a.show();
    }

    /**
     * Set color scheme for the app
     * @param event Action event for changing theme
     */
    @FXML
    public void onSetColor(ActionEvent event) {
        RadioMenuItem selected = (RadioMenuItem) event.getSource();

        String themePath;
        if (selected.getText().equals("Dark mode")) {
            themePath = "/hi/event/vidmot/css/dark-mode.css";
        } else {
            themePath = "/hi/event/vidmot/css/light-mode.css";
        }

        // Set the theme globally
        EventManagerApplication.setTheme(themePath);

        // Apply it to the current scene
        Scene scene = selected.getParentPopup().getOwnerWindow().getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(themePath).toExternalForm());

        // 🆕 Apply theme-specific logo in LoginController
        Stage stage = (Stage) scene.getWindow();
        if (stage.getScene().getRoot().getUserData() instanceof LoginController loginController) {
            loginController.applyTheme(themePath);
        }

    }


    /**
     * Open Account settings window and display logged-in user's info
     * @param event Action event to open account settings
     */
    @FXML
    void onAccount(ActionEvent event) {
        // Get the logged-in user from the session
        User loggedInUser = UserSession.getLoggedInUser();

        if (loggedInUser != null) {
            // Fetch values from the JavaFX properties
            String username = loggedInUser.usernameProperty().get();
            String email = loggedInUser.emailProperty().get();
            String name = loggedInUser.nameProperty().get();

            // Create a message with the user's information
            String userInfo = "Username: " + username + "\n" +
                    "Email: " + email + "\n" +
                    "Name: " + name;

            // Show user info in an alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account Information");
            alert.setHeaderText("Logged-In User Information");
            alert.setContentText(userInfo);
            alert.showAndWait();
        } else {
            // If no user is logged in
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Account Settings");
            alert.setHeaderText("Error");
            alert.setContentText("No user is currently logged in.");
            alert.showAndWait();
        }
    }

    @FXML
    public void onLogout(ActionEvent actionEvent) {
        UserSession.clearLoggedInUser();
        Stage currentStage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        currentStage.close();

        try {
            switchToLoginScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToLoginScreen() throws IOException {
        // Load the login screen FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hi/event/vidmot/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);

        // Get the current theme path (light or dark mode)
        String currentTheme = EventManagerApplication.getTheme(); // Get the theme set in the app

        // Apply the current theme to the login scene
        scene.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm()); // Apply the theme

        // Set the application reference in the controller
        LoginController loginController = fxmlLoader.getController();
        loginController.setApplication(EventManagerApplication.getApplicationInstance()); // Ensure correct app reference

        // Apply the correct logo based on the theme
        loginController.applyTheme(currentTheme); // Update logo as per theme (light/dark)

        // Show the login screen
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    // Private methods

    /**
     * Returns the EventManagerController
     * @return EventManagerController
     */
    private EventManagerController getEventManagerController() {
        return EventManagerApplication.getController();
    }
}
