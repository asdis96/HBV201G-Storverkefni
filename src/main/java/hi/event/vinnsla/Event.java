package hi.event.vinnsla;

import com.google.gson.annotations.Expose;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {

    // JavaFX properties
    private SimpleBooleanProperty selected;
    private SimpleStringProperty title;
    private SimpleObjectProperty<LocalDate> date;
    private SimpleObjectProperty<Group> group;
    private SimpleObjectProperty<EventStatus> status;
    private SimpleObjectProperty<Media> videoMedia; // Media for video
    private SimpleObjectProperty<Image> imageMedia; // Image for image
    private SimpleObjectProperty<LocalTime> time; // Time of the event
    private SimpleStringProperty description; // Description of the event

    // Gson (Expose) fields
    @Expose
    private boolean selectedForSerialization;
    @Expose
    private String titleForSerialization;
    @Expose
    private LocalDate dateForSerialization;
    @Expose
    private Group groupForSerialization;
    @Expose
    private EventStatus statusForSerialization;
    @Expose
    private Media videoMediaForSerialization;
    @Expose
    private Image imageMediaForSerialization;
    @Expose
    private LocalTime timeForSerialization;
    @Expose
    private String descriptionForSerialization;

    // Constructor
    public Event(boolean selected, String title, LocalDate date, Group group, EventStatus status,
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

        // These are used for serialization purposes
        this.selectedForSerialization = selected;
        this.titleForSerialization = title;
        this.dateForSerialization = date;
        this.groupForSerialization = group;
        this.statusForSerialization = status;
        this.videoMediaForSerialization = videoMedia;
        this.imageMediaForSerialization = imageMedia;
        this.timeForSerialization = time;
        this.descriptionForSerialization = description;
    }

    // Getters and setters for JavaFX properties (required for FXML binding)

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public SimpleObjectProperty<Group> groupProperty() {
        return group;
    }

    public Group getGroup() {
        return group.get();
    }

    public void setGroup(Group group) {
        this.group.set(group);
    }

    public SimpleObjectProperty<EventStatus> statusProperty() {
        return status;
    }

    public EventStatus getStatus() {
        return status.get();
    }

    public void setStatus(EventStatus status) {
        this.status.set(status);
    }

    public SimpleObjectProperty<Media> videoMediaProperty() {
        return videoMedia;
    }

    public Media getVideoMedia() {
        return videoMedia.get();
    }

    public void setVideoMedia(Media videoMedia) {
        this.videoMedia.set(videoMedia);
    }

    public SimpleObjectProperty<Image> imageMediaProperty() {
        return imageMedia;
    }

    public Image getImageMedia() {
        return imageMedia.get();
    }

    public void setImageMedia(Image imageMedia) {
        this.imageMedia.set(imageMedia);
    }

    public SimpleObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public LocalTime getTime() {
        return time.get();
    }

    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    // Gson serialization - Not used for binding but helps with JSON storage

    public boolean isSelectedForSerialization() {
        return selectedForSerialization;
    }

    public void setSelectedForSerialization(boolean selectedForSerialization) {
        this.selectedForSerialization = selectedForSerialization;
    }

    public String getTitleForSerialization() {
        return titleForSerialization;
    }

    public void setTitleForSerialization(String titleForSerialization) {
        this.titleForSerialization = titleForSerialization;
    }

    public LocalDate getDateForSerialization() {
        return dateForSerialization;
    }

    public void setDateForSerialization(LocalDate dateForSerialization) {
        this.dateForSerialization = dateForSerialization;
    }

    public Group getGroupForSerialization() {
        return groupForSerialization;
    }

    public void setGroupForSerialization(Group groupForSerialization) {
        this.groupForSerialization = groupForSerialization;
    }

    public EventStatus getStatusForSerialization() {
        return statusForSerialization;
    }

    public void setStatusForSerialization(EventStatus statusForSerialization) {
        this.statusForSerialization = statusForSerialization;
    }

    public Media getVideoMediaForSerialization() {
        return videoMediaForSerialization;
    }

    public void setVideoMediaForSerialization(Media videoMediaForSerialization) {
        this.videoMediaForSerialization = videoMediaForSerialization;
    }

    public Image getImageMediaForSerialization() {
        return imageMediaForSerialization;
    }

    public void setImageMediaForSerialization(Image imageMediaForSerialization) {
        this.imageMediaForSerialization = imageMediaForSerialization;
    }

    public LocalTime getTimeForSerialization() {
        return timeForSerialization;
    }

    public void setTimeForSerialization(LocalTime timeForSerialization) {
        this.timeForSerialization = timeForSerialization;
    }

    public String getDescriptionForSerialization() {
        return descriptionForSerialization;
    }

    public void setDescriptionForSerialization(String descriptionForSerialization) {
        this.descriptionForSerialization = descriptionForSerialization;
    }
}
