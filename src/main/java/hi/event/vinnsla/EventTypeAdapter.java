package hi.event.vinnsla;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventTypeAdapter implements JsonSerializer<Event>, JsonDeserializer<Event> {

    @Override
    public JsonElement serialize(Event event, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", event.getTitle()); // use getTitle instead of getName
        jsonObject.addProperty("date", event.getDate().toString()); // serialize LocalDate to string
        jsonObject.addProperty("selected", event.getSelectedForSerialization()); // serialize boolean value
        jsonObject.addProperty("time", event.getTime().toString()); // serialize LocalTime to string
        jsonObject.addProperty("description", event.getDescriptionForSerialization()); // serialize description

        // Add more fields if needed, like Group, EventStatus, etc.
        return jsonObject;
    }

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String title = jsonObject.get("title").getAsString();
        String dateString = jsonObject.get("date").getAsString();
        LocalDate date = LocalDate.parse(dateString); // Parse string to LocalDate
        boolean selected = jsonObject.get("selected").getAsBoolean();
        String timeString = jsonObject.get("time").getAsString();
        LocalTime time = LocalTime.parse(timeString); // Parse string to LocalTime
        String description = jsonObject.get("description").getAsString();

        // Assuming Group and EventStatus are enums or similar classes:
        Group group = Group.valueOf(jsonObject.get("group").getAsString()); // Example for Group
        EventStatus status = EventStatus.valueOf(jsonObject.get("status").getAsString()); // Example for EventStatus

        return new Event(selected, title, date, group, status, null, null, time, description); // adjust constructors as needed
    }
}
