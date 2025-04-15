module hi.event {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.json;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;

    opens hi.event.vidmot to javafx.fxml;
    exports hi.event.vidmot;
    exports hi.event.vinnsla;
}
