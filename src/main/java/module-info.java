module hi.event {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires java.desktop;
    requires org.json;

    opens hi.event.vidmot to javafx.fxml;
    exports hi.event.vidmot;
    exports hi.event.vinnsla;
}
