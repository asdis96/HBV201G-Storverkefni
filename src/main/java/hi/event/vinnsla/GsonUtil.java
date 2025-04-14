package hi.event.vinnsla;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Event.class, new EventTypeAdapter())
                .create();
    }
}
