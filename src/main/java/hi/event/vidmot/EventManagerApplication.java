package hi.event.vidmot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
public class EventManagerApplication extends Application {

    private static EventManagerController controller;

    private static EventManagerApplication instance;


    // Default theme path (light mode)
    private static String currentTheme = "/hi/event/vidmot/css/light-mode.css";

    public static void setTheme(String themePath) {
        currentTheme = themePath;
    }

    public static String getTheme() {
        return currentTheme;
    }


    @Override
    public void start(Stage stage) throws IOException {
        // Load the login view first
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hi/event/vidmot/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);

        // Apply current theme to login scene
        scene.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());

        // Set the application reference in the controller
        LoginController loginController = fxmlLoader.getController();
        loginController.setApplication(this);

        // Apply the correct theme-specific logo
        loginController.applyTheme(currentTheme);

        // Show login screen
        stage.setTitle("Login - Event Manager");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Switch to the EventManager view after a successful login
     */
    public void switchToEventManager() throws IOException {
        instance = this;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hi/event/vidmot/eventmanager-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);

        // Apply current theme
        scene.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());

        EventManagerController eventManagerController = fxmlLoader.getController();
        setController(eventManagerController);

        Stage stage = new Stage();
        stage.setTitle("Event Manager");
        stage.setScene(scene);
        stage.show();
    }



    /**
     * Set the static controller variable (this is called after login)
     */
    static private void setController(EventManagerController controller) {
        EventManagerApplication.controller = controller;
    }

    /**
     * Get the controller for the EventManager
     */
    public static EventManagerController getController() {
        return controller;
    }

    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        launch();
    }

    // Static method to get the instance of the application
    public static EventManagerApplication getApplicationInstance() {
        return instance;
    }

    // Method to apply theme to alerts
    public static void applyStylesheetToAlert(Alert alert) {
        Platform.runLater(() -> {
            if (alert.getDialogPane() != null && alert.getDialogPane().getScene() != null) {
                String currentTheme = getTheme();
                alert.getDialogPane().getScene().getStylesheets().add(
                        EventManagerApplication.class.getResource(currentTheme).toExternalForm());
            }
        });
    }

}
