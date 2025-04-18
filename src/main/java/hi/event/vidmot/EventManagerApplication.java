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
 *  Description  :This class represents the main application for the Event Manager system. It extends
 *   the `Application` class from JavaFX and serves as the entry point for the application.
 *   The application starts by displaying the login screen and provides functionality to
 *  switch to the Event Manager screen after a successful login. The class also handles
 *  theme management (light/dark mode) and ensures that the selected theme is applied
 *  throughout the application.
 *
 *****************************************************************************/
public class EventManagerApplication extends Application {

    private static EventManagerController controller;

    private static EventManagerApplication instance;

    private static String currentTheme = "/hi/event/vidmot/css/light-mode.css";

    /**
     * Sets the theme path for the application.
     *
     * @param themePath the path to the new theme's CSS file.
     */
    public static void setTheme(String themePath) {
        currentTheme = themePath;
    }

    /**
     * Returns the current theme path for the application.
     *
     * @return the current theme's CSS file path.
     */
    public static String getTheme() {
        return currentTheme;
    }


    /**
     * The entry point of the application. Loads and displays the login screen.
     * Applies the current theme to the login screen.
     *
     * @param stage the primary stage for the application.
     * @throws IOException if there is an error loading the FXML file for the login view.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hi/event/vidmot/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);

        scene.getStylesheets().add(getClass().getResource(currentTheme).toExternalForm());

        LoginController loginController = fxmlLoader.getController();
        loginController.setApplication(this);

        loginController.applyTheme(currentTheme);

        stage.setTitle("Login - Event Manager");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Switches to the EventManager view after a successful login.
     *
     * @throws IOException if there is an error loading the FXML file for the Event Manager view.
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
     * Sets the static controller variable (called after a successful login) for the EventManager.
     *
     * @param controller the EventManagerController to set.
     */
    static private void setController(EventManagerController controller) {
        EventManagerApplication.controller = controller;
    }

    /**
     * Returns the controller for the EventManager.
     *
     * @return the current EventManagerController instance.
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

    /**
     * Returns the current instance of the EventManagerApplication.
     *
     * @return the instance of the EventManagerApplication.
     */
    public static EventManagerApplication getApplicationInstance() {
        return instance;
    }

    /**
     * Applies the current theme to alert dialogs.
     * This ensures that the alerts match the selected theme (light/dark).
     *
     * @param alert the alert dialog to which the theme should be applied.
     */
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
