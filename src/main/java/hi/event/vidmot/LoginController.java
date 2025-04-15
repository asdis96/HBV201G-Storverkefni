package hi.event.vidmot;

import hi.event.vinnsla.User;
import hi.event.vinnsla.UserSession;
import hi.event.vinnsla.UserStorage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    private Button loginButton;


    /**
     * Set the application reference (needed for switching to EventManager)
     */
    private EventManagerApplication application;

    public void setApplication(EventManagerApplication application) {
        this.application = application;
    }

    @FXML
    private void onLoginButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Use UserStorage to validate login credentials
        if (UserStorage.validateLogin(username, password)) {
            // Successful login logic here
            System.out.println("Login successful!");

            try {
                // Load the EventManager view (EventManagerController)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/hi/event/vidmot/eventmanager-view.fxml"));
                Parent root = loader.load();

                // Get the controller for the EventManager view
                EventManagerController eventManagerController = loader.getController();

                // Get the logged-in user from the UserStorage
                User loggedInUser = UserStorage.getUserByUsername(username);

                // Set the logged-in user in the UserSession class
                UserSession.setLoggedInUser(loggedInUser);

                // Pass the current user to the EventManagerController
                eventManagerController.setCurrentUser(loggedInUser);

                // Show the EventManager window
                Stage eventManagerStage = new Stage();
                eventManagerStage.setTitle("Event Manager");
                eventManagerStage.setScene(new Scene(root));
                eventManagerStage.show();

                // Close the login window
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error loading event manager.");
            }

        } else {
            showAlert("Invalid credentials.");
        }
    }


    @FXML
    private void onSignUpLinkClicked() {
        try {
            // Load the SignUp FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hi/event/vidmot/signup-view.fxml"));
            Stage signUpStage = new Stage();
            signUpStage.setTitle("Sign Up");

            // Set the scene with the loaded FXML
            signUpStage.setScene(new Scene(loader.load()));

            // Show the SignUp dialog window
            signUpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Failed");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
