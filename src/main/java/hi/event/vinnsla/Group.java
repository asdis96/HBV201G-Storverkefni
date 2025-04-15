package hi.event.vinnsla;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/******************************************************************************
 *  Nafn    : Ásdís Stefánsdóttir
 *  T-póstur: ahl4@hi.is
 *  Lýsing  : Vinnsluklasi fyrir flokk af viðburðum
 *
 *
 *****************************************************************************/
public enum Group {

    EDUCATION("Education"),
    ENTERTAINMENT("Entertainment"),
    FAMILY("Family");

    private final String groupName;

    Group(String groupName) {
        this.groupName =groupName ;
    }

    /**
     * skilar nafni flokksins
     * @return nafn flokksins
     */


    @Override
    public String toString() {
        return groupName;
    }

    @JsonValue
    public String getStatusName() {
        return groupName;
    }
    // Jackson deserialization method
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
