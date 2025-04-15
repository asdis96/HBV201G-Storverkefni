package hi.event.vidmot;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioMenuItem;

/******************************************************************************
 *  Nafn    : Ásdís Halldóra L Stefánsdóttir
 *  T-póstur: ahl4@hi.is
 *
 *  Lýsing  : Controller fyrir menu-inn. hætta, about, account , color
 *
 *
 *****************************************************************************/
public class MenuController {

    /**
     * Hættir í forritinu
     * @param event ónotað
     */
    @FXML
    void onExit(ActionEvent event) {
        getEventManagerController().saveEventsToStorage();
        Platform.exit();
    }

    /**
     * Gefur upplýsingar um forritið
     * @param event ónotað
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
     * @param event Action event for changing color
     */
    @FXML
    void onSetColor(ActionEvent event) {
        // Show color options (you can expand this logic further)
        getEventManagerController().breytaLit((RadioMenuItem) event.getSource());
    }

    /**
     * Open Account settings window (Example placeholder method)
     * @param event Action event to open account settings
     */
    @FXML
    void onAccount(ActionEvent event) {
        // Here you would implement opening the account settings screen
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account settings functionality goes here.", ButtonType.OK);
        alert.setTitle("Account Settings");
        alert.setHeaderText("Account");
        alert.showAndWait();
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
