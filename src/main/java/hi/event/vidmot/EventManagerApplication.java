package hi.event.vidmot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EventManagerApplication extends Application {

    private static EventManagerController controller;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the login view first
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hi/event/vidmot/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);

        // Set the application reference (if needed)
        LoginController loginController = fxmlLoader.getController();
        loginController.setApplication(this);

        // Show login screen
        stage.setTitle("Login - Event Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch to the EventManager view after a successful login
     */
    public void switchToEventManager() throws IOException {
        // Load the EventManager view (main screen)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hi/event/vidmot/eventmanager-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);

        // Get the EventManagerController
        EventManagerController eventManagerController = fxmlLoader.getController();

        // Set the controller using the setter
        setController(eventManagerController);

        // Now switch to the EventManager view
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
}
