package hi.event.vinnsla;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.time.LocalDate;
import java.time.LocalTime;

/******************************************************************************
 *  Nafn    : Ásdís Stefánsdóttir
 *  T-póstur: ahl4@hi.is
 *  Lýsing  : Vinnslu (Model) klasi fyrir viðburði
 *****************************************************************************/
public class Event {

    public SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
    private SimpleStringProperty title;
    private SimpleObjectProperty<LocalDate> date;
    private SimpleObjectProperty<Group> group;
    private SimpleObjectProperty<EventStatus> status;
    private SimpleObjectProperty<Media> videoMedia; // Media for video
    private SimpleObjectProperty<Image> imageMedia; // Image for image
    private SimpleObjectProperty<LocalTime> time; // Time of the event
    private SimpleStringProperty description; // Description of the event

    // Constructor
    public Event(Boolean selected, String title, LocalDate date, Group group, EventStatus status,
                 Media videoMedia, Image imageMedia, LocalTime time, String description) {
        this.selected = new SimpleBooleanProperty(selected);
        this.title = new SimpleStringProperty(title);
        this.date = new SimpleObjectProperty<>(date);
        this.group = new SimpleObjectProperty<>(group);
        this.status = new SimpleObjectProperty<>(status);
        this.videoMedia = new SimpleObjectProperty<>(videoMedia);
        this.imageMedia = new SimpleObjectProperty<>(imageMedia);
        this.time = new SimpleObjectProperty<>(time);
        this.description = new SimpleStringProperty(description);
    }

    // Getter for 'selected' property (SimpleBooleanProperty)
    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    // Getter for 'title' property (SimpleStringProperty)
    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    // Getter for 'date' property (SimpleObjectProperty<LocalDate>)
    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    // Getter for 'group' property (SimpleObjectProperty<Group>)
    public SimpleObjectProperty<Group> groupProperty() {
        return group;
    }

    public Group getGroup() {
        return group.get();
    }

    public void setGroup(Group group) {
        this.group.set(group);
    }

    // Getter for 'status' property (SimpleObjectProperty<EventStatus>)
    public SimpleObjectProperty<EventStatus> statusProperty() {
        return status;
    }

    public EventStatus getStatus() {
        return status.get();
    }

    public void setStatus(EventStatus status) {
        this.status.set(status);
    }

    // Getter for 'videoMedia' property (SimpleObjectProperty<Media>)
    public SimpleObjectProperty<Media> videoMediaProperty() {
        return videoMedia;
    }

    public Media getVideoMedia() {
        return videoMedia.get();
    }

    public void setVideoMedia(Media videoMedia) {
        this.videoMedia.set(videoMedia);
    }

    // Getter for 'imageMedia' property (SimpleObjectProperty<Image>)
    public SimpleObjectProperty<Image> imageMediaProperty() {
        return imageMedia;
    }

    public Image getImageMedia() {
        return imageMedia.get();
    }

    public void setImageMedia(Image imageMedia) {
        this.imageMedia.set(imageMedia);
    }

    // Getter for 'time' property (SimpleObjectProperty<LocalTime>)
    public SimpleObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public LocalTime getTime() {
        return time.get();
    }

    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    // Getter for 'description' property (SimpleStringProperty)
    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    // Getters for serialization
    public boolean getSelectedForSerialization() {
        return selected.get();
    }

    public void setSelectedFromSerialization(boolean selected) {
        this.selected.set(selected);
    }

    public String getTitleForSerialization() {
        return title.get();
    }

    public void setTitleFromSerialization(String title) {
        this.title.set(title);
    }

    public LocalDate getDateForSerialization() {
        return date.get();
    }

    public void setDateFromSerialization(LocalDate date) {
        this.date.set(date);
    }

    public Group getGroupForSerialization() {
        return group.get();
    }

    public void setGroupFromSerialization(Group group) {
        this.group.set(group);
    }

    public EventStatus getStatusForSerialization() {
        return status.get();
    }

    public void setStatusFromSerialization(EventStatus status) {
        this.status.set(status);
    }

    public Media getVideoMediaForSerialization() {
        return videoMedia.get();
    }

    public void setVideoMediaFromSerialization(Media videoMedia) {
        this.videoMedia.set(videoMedia);
    }

    public Image getImageMediaForSerialization() {
        return imageMedia.get();
    }

    public void setImageMediaFromSerialization(Image imageMedia) {
        this.imageMedia.set(imageMedia);
    }

    public LocalTime getTimeForSerialization() {
        return time.get();
    }

    public void setTimeFromSerialization(LocalTime time) {
        this.time.set(time);
    }

    public String getDescriptionForSerialization() {
        return description.get();
    }

    public void setDescriptionFromSerialization(String description) {
        this.description.set(description);
    }
}
