package hi.event.vinnsla;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 *
 *  Description  : Enum representing the status of an event.
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

    /**
     * Converts a string to its corresponding EventStatus enum.
     * This method is used by Jackson during deserialization.
     *
     * @param status The string representation of the status.
     * @return The corresponding EventStatus enum.
     * @throws IllegalArgumentException If the provided string does not match any status.
     */
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
