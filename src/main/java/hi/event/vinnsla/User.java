package hi.event.vinnsla;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleStringProperty;
/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 *
 *  Description  : Represents a user with JavaFX properties for FXML bindings and plain fields for JSON serialization.
 *  This class provides methods for serialization, deserialization, and JavaFX property management.
 *
 *
 *****************************************************************************/
public class User {

    // JavaFX properties (for FXML bindings)
    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private SimpleStringProperty email;
    private SimpleStringProperty name;

    // Plain fields (for Jackson serialization)
    private String usernameValue;
    private String passwordValue;
    private String emailValue;
    private String nameValue;

    @JsonCreator
    public User(@JsonProperty("usernameValue") String username,
                @JsonProperty("passwordValue") String password,
                @JsonProperty("emailValue") String email,
                @JsonProperty("nameValue") String name) {

        this.usernameValue = username != null ? username : "";
        this.passwordValue = password != null ? password : "";
        this.emailValue = email != null ? email : "";
        this.nameValue = name != null ? name : "";


        this.username = new SimpleStringProperty(usernameValue);
        this.password = new SimpleStringProperty(passwordValue);
        this.email = new SimpleStringProperty(emailValue);
        this.name = new SimpleStringProperty(nameValue);
    }

    // Getter and setter methods for JavaFX properties
    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }


    public String getUsernameValue() {
        return usernameValue;
    }

    public void setUsernameValue(String usernameValue) {
        this.usernameValue = usernameValue;
        this.username.set(usernameValue);
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    public void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
        this.password.set(passwordValue);
    }

    public String getEmailValue() {
        return emailValue;
    }

    public void setEmailValue(String emailValue) {
        this.emailValue = emailValue;
        this.email.set(emailValue);
    }

    public String getNameValue() {
        return nameValue;
    }

    public void setNameValue(String nameValue) {
        this.nameValue = nameValue;
        this.name.set(nameValue);
    }


    @JsonIgnore
    public String getFormattedUsernameForFXML() {
        return username.get();
    }

    @JsonIgnore
    public String getFormattedEmailForFXML() {
        return email.get();
    }

    @JsonIgnore
    public String getFormattedNameForFXML() {
        return name.get();
    }

    @JsonIgnore
    public String getFormattedPasswordForFXML() {
        return password.get();
    }

    /**
     * Converts this {@link User} object to a JSON string.
     *
     * @return the JSON string representation of the user
     */
    public String toJSON() {
        return "{"
                + "\"username\":\"" + usernameValue + "\","
                + "\"password\":\"" + passwordValue + "\","
                + "\"email\":\"" + emailValue + "\","
                + "\"name\":\"" + nameValue + "\""
                + "}";
    }

    /**
     * Creates a new {@link User} object from a JSON string.
     *
     * @param json the JSON string to parse
     * @return a new {@link User} object
     */
    public static User fromJSON(String json) {
        String[] parts = json.replace("{", "").replace("}", "").split(",");
        String username = parts[0].split(":")[1].replace("\"", "");
        String password = parts[1].split(":")[1].replace("\"", "");
        String email = parts[2].split(":")[1].replace("\"", "");
        String name = parts[3].split(":")[1].replace("\"", "");

        return new User(username, password, email, name);
    }

    /**
     * Populates the JavaFX properties from the plain field values.
     */
    public void populateFromPlainValues() {
        this.username.set(this.usernameValue);
        this.password.set(this.passwordValue);
        this.email.set(this.emailValue);
        this.name.set(this.nameValue);
    }

    /**
     * Extracts the plain field values from the JavaFX properties.
     */
    public void extractToPlainValues() {
        this.usernameValue = this.username.get();
        this.passwordValue = this.password.get();
        this.emailValue = this.email.get();
        this.nameValue = this.name.get();
    }
}
