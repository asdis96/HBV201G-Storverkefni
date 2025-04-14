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
        FXMLLoader fxmlLoader = new FXMLLoader(EventManagerApplication
                .class.getResource("/hi/event/vidmot/eventmanager-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        EventManagerApplication.setController (fxmlLoader.getController());
        stage.setTitle("Viðburðastjóri");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * setja static breytu controller
     * @param controller aðal controllerinn
     */
    static private void setController(EventManagerController controller) {
        EventManagerApplication.controller = controller;
    }

    /**
     * Skilar controller fyrir EventManager úr static breytu
     * @return controller fyrir EventManager
     */
    public static EventManagerController getController() {
        return controller;
    }

    /**
     * Ræsir forritið
     * @param args ónotað
     */
    public static void main(String[] args) {
        launch();
    }
}
