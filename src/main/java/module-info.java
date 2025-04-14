module hi.event {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires com.google.gson;
    requires javafx.base;


    opens hi.event.vidmot to javafx.fxml;
    exports hi.event.vidmot;
    exports hi.event.vinnsla;
}
