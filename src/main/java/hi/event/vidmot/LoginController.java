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

    public void setApplication(EventManagerApplication application) {
        this.application = application;
    }

    public void applyTheme(String themePath) {
        String logoPath = themePath.contains("dark")
                ? "/hi/event/vidmot/media/dark-logo.jpg"
                : "/hi/event/vidmot/media/light-logo.jpg"
                ;

        logoImage.setImage(new Image(getClass().getResource(logoPath).toExternalForm()));
    }

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
        EventManagerApplication.applyStylesheetToAlert(alert);
        alert.showAndWait();
    }
}
