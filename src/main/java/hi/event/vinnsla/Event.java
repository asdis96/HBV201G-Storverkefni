package hi.event.vinnsla;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Event {

    private SimpleBooleanProperty selected;
    private SimpleStringProperty title;
    private SimpleObjectProperty<LocalDate> date;
    private SimpleObjectProperty<Group> group;
    private SimpleObjectProperty<EventStatus> status;
    // Jackson will not serialize these properties
    @JsonIgnore
    private SimpleObjectProperty<Media> videoMedia;
    @JsonIgnore
    private SimpleObjectProperty<Image> imageMedia;
    private SimpleObjectProperty<LocalTime> time;
    private SimpleStringProperty description;

    private SimpleStringProperty videoMediaPath;
    private SimpleStringProperty imageMediaPath;

    // Jackson-compatible fields (plain types for serialization/deserialization)
    private Boolean selectedValue;
    private String titleValue;
    private LocalDate dateValue;
    private Group groupValue;
    private EventStatus statusValue;
    private String videoMediaPathValue;
    private String imageMediaPathValue;
    private LocalTime timeValue;
    private String descriptionValue;

    @JsonCreator
    public Event(@JsonProperty("selectedValue") Boolean selected,
                 @JsonProperty("titleValue") String title,
                 @JsonProperty("dateValue") LocalDate date,
                 @JsonProperty("groupValue") Group group,
                 @JsonProperty("statusValue") EventStatus status,
                 @JsonProperty("videoMediaPathValue") String videoMediaPath,
                 @JsonProperty("imageMediaPathValue") String imageMediaPath,
                 @JsonProperty("timeValue") LocalTime time,
                 @JsonProperty("descriptionValue") String description) {

        // Handle nulls and default values here
        this.selectedValue = (selected != null) ? selected : false;
        this.titleValue = title != null ? title : "";
        this.dateValue = date != null ? date : LocalDate.now();
        this.groupValue = group != null ? group : Group.ENTERTAINMENT;
        this.statusValue = status != null ? status : EventStatus.ACTIVE;
        this.videoMediaPathValue = videoMediaPath != null ? videoMediaPath : "";
        this.imageMediaPathValue = imageMediaPath != null ? imageMediaPath : "";
        this.timeValue = time != null ? time : LocalTime.now();
        this.descriptionValue = description != null ? description : "";

        // Initialize JavaFX properties
        this.selected = new SimpleBooleanProperty(selectedValue);
        this.title = new SimpleStringProperty(titleValue);
        this.date = new SimpleObjectProperty<>(dateValue);
        this.group = new SimpleObjectProperty<>(groupValue);
        this.status = new SimpleObjectProperty<>(statusValue);
        this.videoMedia = new SimpleObjectProperty<>();
        this.imageMedia = new SimpleObjectProperty<>();
        this.time = new SimpleObjectProperty<>(timeValue);
        this.description = new SimpleStringProperty(descriptionValue);
        this.videoMediaPath = new SimpleStringProperty(videoMediaPathValue);
        this.imageMediaPath = new SimpleStringProperty(imageMediaPathValue);
    }

    // Getter and setter methods for JavaFX properties
    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public SimpleObjectProperty<Group> groupProperty() {
        return group;
    }

    public SimpleObjectProperty<EventStatus> statusProperty() {
        return status;
    }

    public SimpleObjectProperty<Media> videoMediaProperty() {
        return videoMedia;
    }

    public SimpleObjectProperty<Image> imageMediaProperty() {
        return imageMedia;
    }

    public SimpleObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public SimpleStringProperty videoMediaPathProperty() {
        return videoMediaPath;
    }

    public SimpleStringProperty imageMediaPathProperty() {
        return imageMediaPath;
    }

    // Jackson getters and setters for plain fields
    public Boolean getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Boolean selectedValue) {
        this.selectedValue = selectedValue;
    }

    public String getTitleValue() {
        return titleValue;
    }

    public void setTitleValue(String titleValue) {
        this.titleValue = titleValue;
    }


    public Group getGroupValue() {
        return groupValue;
    }

    public void setGroupValue(Group groupValue) {
        this.groupValue = groupValue;
    }

    public EventStatus getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(EventStatus statusValue) {
        this.statusValue = statusValue;
    }

    public String getVideoMediaPathValue() {
        return videoMediaPathValue;
    }

    public void setVideoMediaPathValue(String videoMediaPathValue) {
        this.videoMediaPathValue = videoMediaPathValue;
    }

    public String getImageMediaPathValue() {
        return imageMediaPathValue;
    }

    public void setImageMediaPathValue(String imageMediaPathValue) {
        this.imageMediaPathValue = imageMediaPathValue;
    }

    public Image getImageMedia() {
        return imageMedia.get();
    }
    public void setImageMedia(Image imageMedia) {
        this.imageMedia.set(imageMedia);
    }

    public Media getVideoMedia() {
        return videoMedia.get();
    }
    public void setVideoMedia(Media videoMedia) {
        this.videoMedia.set(videoMedia);
    }

    @JsonIgnore
    public String getFormattedTimeForFXML() {
        if (timeValue != null) {
            return timeValue.format(DateTimeFormatter.ofPattern("HH:mm")); // Exclude nanoseconds
        }
        return null;
    }

    @JsonIgnore
    // FXML formatting: Format LocalDate as "DD MMMM YYYY" (e.g., "15 April 2025")
    public String getFormattedDateForFXML() {
        if (dateValue != null) {
            return dateValue.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")); // Custom format for FXML
        }
        return null;
    }

    public void loadImageMedia() {
        String imagePath = imageMediaPathValue;  // This should be something like "hi/event/vidmot/media/monk.JPG"
        URL imageUrl = getClass().getClassLoader().getResource(imagePath);

        if (imageUrl != null) {
            Image image = new Image(imageUrl.toString());
            setImageMedia(image);  // Assuming setImageMedia() stores the loaded image
        } else {
            System.out.println("Image not found at: " + imagePath);
            setImageMedia(null);  // Optionally handle no image being available
        }
    }

    public void loadVideoMedia() {
        String videoPath = videoMediaPathValue;  // This should be something like "hi/event/vidmot/media/world_fixed.mp4"
        URL videoUrl = getClass().getClassLoader().getResource(videoPath);

        if (videoUrl != null) {
            Media videoMedia = new Media(videoUrl.toString());
            setVideoMedia(videoMedia);  // Assuming setVideoMedia() stores the loaded video media
        } else {
            System.out.println("Video not found at: " + videoPath);
            setVideoMedia(null);  // Optionally handle no video being available
        }
    }



    public LocalDate getDateValue() {
        return dateValue;
    }

    public void setDateValue(LocalDate dateValue) {
        this.dateValue = dateValue;
    }

    public LocalTime getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(LocalTime timeValue) {
        this.timeValue = timeValue;
    }

    public String getDescriptionValue() {
        return descriptionValue;
    }

    public void setDescriptionValue(String descriptionValue) {
        this.descriptionValue = descriptionValue;
    }

    // Method to populate JavaFX properties from plain values
    public void populateFromPlainValues() {
        this.selected.set(this.selectedValue);
        this.title.set(this.titleValue);
        this.date.set(this.dateValue);
        this.group.set(this.groupValue);
        this.status.set(this.statusValue);
        this.time.set(this.timeValue);
        this.description.set(this.descriptionValue);
        this.videoMediaPath.set(this.videoMediaPathValue);
        this.imageMediaPath.set(this.imageMediaPathValue);
    }

    // Method to extract plain values from JavaFX properties
    public void extractToPlainValues() {
        this.selectedValue = this.selected.get();
        this.titleValue = this.title.get();
        this.dateValue = this.date.get();
        this.groupValue = this.group.get();
        this.statusValue = this.status.get();
        this.timeValue = this.time.get();
        this.descriptionValue = this.description.get();
        this.videoMediaPathValue = this.videoMediaPath.get();
        this.imageMediaPathValue = this.imageMediaPath.get();
    }
}
