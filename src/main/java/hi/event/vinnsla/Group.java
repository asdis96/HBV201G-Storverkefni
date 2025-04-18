package hi.event.vinnsla;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 *
 *  Description  : Enum class to store event groups
 *
 *
 *****************************************************************************/
public  enum Group {

    EDUCATION("Education"),
    ENTERTAINMENT("Entertainment"),
    FAMILY("Family");

    private final String groupName;

    Group(String groupName) {
        this.groupName =groupName ;
    }

    @Override
    public String toString() {
        return groupName;
    }

    @JsonValue
    public String getStatusName() {
        return groupName;
    }

    /**
     * Converts a string to its corresponding Group enum.
     * This method is used by Jackson during deserialization.
     *
     * @param name The string representation of the event group.
     * @return The corresponding Group enum.
     * @throws IllegalArgumentException If the provided string does not match any group.
     */
    @JsonCreator
    public static Group fromString(String name) {
        for (Group group : Group.values()) {
            if (group.groupName.equalsIgnoreCase(name)) {
                return group;
            }
        }
        throw new IllegalArgumentException("Unknown group: " + name);
    }
}
