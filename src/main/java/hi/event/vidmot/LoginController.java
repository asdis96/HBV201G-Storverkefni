package hi.event.vidmot;

import hi.event.vinnsla.User;
import hi.event.vinnsla.UserSession;
import hi.event.vinnsla.UserStorage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 *
 *  Description  :  This controller handles the login functionality for the Event Manager application.
 *  It manages user input for logging in, displays the corresponding logo based on the
 *  selected theme, and provides a way to navigate to the signup screen if the user
 *   doesn't have an account. Additionally, it handles error messages in case of invalid
 *  login attempts. Upon a successful login, the user is redirected to the Event Manager
 *  interface, and the login window is closed.
 *
 *
 *****************************************************************************/
public class LoginController {
    @FXML
    public ImageView logoImage;
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

    /**
     * Sets the reference to the application instance.
     *
     * @param application the EventManagerApplication instance that will be used for scene switching.
     */
    public void setApplication(EventManagerApplication application) {
        this.application = application;
    }
    /**
     * Changes the logo depending on the selected theme (light or dark).
     *
     * @param themePath the path to the theme's CSS file, which determines whether it's dark or light theme.
     */
    public void applyTheme(String themePath) {
        String logoPath = themePath.contains("dark")
                ? "/hi/event/vidmot/media/dark-logo.jpg"
                : "/hi/event/vidmot/media/light-logo.jpg"
                ;

        logoImage.setImage(new Image(getClass().getResource(logoPath).toExternalForm()));
    }

    /**
     * Handles the login button click event. Validates the user's credentials and
     * switches to the Event Manager scene if successful. If the login is unsuccessful,
     * an error alert is displayed.
     */
    @FXML
    private void onLoginButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (UserStorage.validateLogin(username, password)) {
            System.out.println("Login successful!");

            try {
                User loggedInUser = UserStorage.getUserByUsername(username);
                UserSession.setLoggedInUser(loggedInUser);

                application.switchToEventManager();
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

    /**
     * Opens the SignUp screen in a new window when the user clicks the sign-up link.
     */
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

    /**
     * Displays an error alert with the specified message.
     *
     * @param message the message to be displayed in the alert
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Failed");
        alert.setHeaderText(null);
        alert.setContentText(message);
        EventManagerApplication.applyStylesheetToAlert(alert);
        alert.showAndWait();
    }
}
