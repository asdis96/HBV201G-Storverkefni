package hi.event.vinnsla;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {

    private SimpleBooleanProperty selected;
    private SimpleStringProperty title;
    private SimpleObjectProperty<LocalDate> date;
    private SimpleObjectProperty<Group> group;
    private SimpleObjectProperty<EventStatus> status;
    private SimpleObjectProperty<Media> videoMedia; // Media for video
    private SimpleObjectProperty<Image> imageMedia; // Image for image
    private SimpleObjectProperty<LocalTime> time; // Time of the event
    private SimpleStringProperty description; // Description of the event

    // Fields for storing relative media paths
    private SimpleStringProperty videoMediaPath;  // Relative path for video
    private SimpleStringProperty imageMediaPath;  // Relative path for image

    // Constructor
    public Event(Boolean selected, String title, LocalDate date, Group group, EventStatus status,
                 Media videoMedia, Image imageMedia, LocalTime time, String description,
                 String videoMediaPath, String imageMediaPath) {
        this.selected = new SimpleBooleanProperty(selected);
        this.title = new SimpleStringProperty(title);
        this.date = new SimpleObjectProperty<>(date);
        this.group = new SimpleObjectProperty<>(group);
        this.status = new SimpleObjectProperty<>(status);
        this.videoMedia = new SimpleObjectProperty<>(videoMedia);
        this.imageMedia = new SimpleObjectProperty<>(imageMedia);
        this.time = new SimpleObjectProperty<>(time);
        this.description = new SimpleStringProperty(description);

        // Initialize the media path properties
        this.videoMediaPath = new SimpleStringProperty(videoMediaPath);
        this.imageMediaPath = new SimpleStringProperty(imageMediaPath);
    }

    // Getter and Setter for selected (Boolean)
    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public Boolean getSelected() {
        return selected.get();
    }

    public void setSelected(Boolean selected) {
        this.selected.set(selected);
    }

    // Getter and Setter for title (String)
    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    // Getter and Setter for date (LocalDate)
    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    // Getter and Setter for group (Group enum)
    public SimpleObjectProperty<Group> groupProperty() {
        return group;
    }

    public Group getGroup() {
        return group.get();
    }

    public void setGroup(Group group) {
        this.group.set(group);
    }

    // Getter and Setter for status (EventStatus enum)
    public SimpleObjectProperty<EventStatus> statusProperty() {
        return status;
    }

    public EventStatus getStatus() {
        return status.get();
    }

    public void setStatus(EventStatus status) {
        this.status.set(status);
    }

    // Getter and Setter for video media (Media)
    public SimpleObjectProperty<Media> videoMediaProperty() {
        return videoMedia;
    }

    public Media getVideoMedia() {
        return videoMedia.get();
    }

    public void setVideoMedia(Media videoMedia) {
        this.videoMedia.set(videoMedia);
    }

    // Getter and Setter for image media (Image)
    public SimpleObjectProperty<Image> imageMediaProperty() {
        return imageMedia;
    }

    public Image getImageMedia() {
        return imageMedia.get();
    }

    public void setImageMedia(Image imageMedia) {
        this.imageMedia.set(imageMedia);
    }

    // Getter and Setter for time (LocalTime)
    public SimpleObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public LocalTime getTime() {
        return time.get();
    }

    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    // Getter and Setter for description (String)
    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    // Getter and Setter for video media path (String)
    public SimpleStringProperty videoMediaPathProperty() {
        return videoMediaPath;
    }

    public String getVideoMediaPath() {
        return videoMediaPath.get();
    }

    public void setVideoMediaPath(String videoMediaPath) {
        this.videoMediaPath.set(videoMediaPath);
    }

    // Getter and Setter for image media path (String)
    public SimpleStringProperty imageMediaPathProperty() {
        return imageMediaPath;
    }

    public String getImageMediaPath() {
        return imageMediaPath.get();
    }

    public void setImageMediaPath(String imageMediaPath) {
        this.imageMediaPath.set(imageMediaPath);
    }
}
