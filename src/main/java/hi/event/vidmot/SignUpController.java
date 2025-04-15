package hi.event.vidmot;

import hi.event.vinnsla.User;
import hi.event.vinnsla.UserStorage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onSignUpButtonClicked() {
        String name = nameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        // Create a new user
        User newUser = new User(username, password, email, name);

        // Save new user using UserStorage class
        UserStorage.addUser(newUser);

        showAlert("Sign Up successful! You can now log in.");

        // Close the current window
        closeWindow();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign Up");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        // Get the Stage from the current button's event source
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
