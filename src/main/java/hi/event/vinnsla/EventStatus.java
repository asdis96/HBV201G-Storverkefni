package hi.event.vinnsla;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/******************************************************************************
 *  Nafn    : Ásdís Stefánsdóttir
 *  T-póstur: ahl4@hi.is
 *  Lýsing  : Vinnsluklasi fyrir status af viðburðum
 *
 *
 *****************************************************************************/

public enum EventStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    CANCELLED("Cancelled");

    private final String statusName;

    EventStatus(String statusName) {
        this.statusName = statusName;
    }

    @JsonValue
    public String getStatusName() {
        return statusName;
    }

    @Override
    public String toString() {
        return statusName;
    }

    @JsonCreator
    public static EventStatus fromString(String status) {
        for (EventStatus eventStatus : EventStatus.values()) {
            if (eventStatus.statusName.equalsIgnoreCase(status)) {
                return eventStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }

}
