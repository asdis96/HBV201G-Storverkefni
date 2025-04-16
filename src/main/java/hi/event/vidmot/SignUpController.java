package hi.event.vidmot;

import hi.event.vinnsla.User;
import hi.event.vinnsla.UserStorage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 *
 *  Description  : Controller for handling user sign-up functionality.
 *  Handles user input, validates the form, creates a new user,
 *  and stores the user in the UserStorage.
 *****************************************************************************/

public class SignUpController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    /**
     * Called when the sign-up button is clicked.
     * Validates the input fields, creates a new user, and stores it.
     * If any field is empty, an alert is shown.
     */
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
        User newUser = new User(username, password, email, name);
        UserStorage.addUser(newUser);

        showAlert("Sign Up successful! You can now log in.");

        closeWindow();
    }

    /**
     * Displays an informational alert with the given message.
     * @param message The message to display in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign Up");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Closes the current window (stage).
     */
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
