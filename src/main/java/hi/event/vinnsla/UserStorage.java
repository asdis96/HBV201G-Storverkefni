package hi.event.vinnsla;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 *
 *  Description  :   This class provides methods to manage user data, including reading, adding,
 *  updating, and validating user login from a JSON file.
 *  <p>
 *  The user data is stored in a JSON file called "users.json", and operations
 *  are performed on that file to load, save, and update user information.
 *
 *
 *
 *****************************************************************************/

public class UserStorage {

    private static final File USERS_FILE = new File("users.json");

    /**
     * Loads all users from the JSON file.
     *
     * @return A list of User objects representing all the users in the storage.
     */
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try {
            if (!USERS_FILE.exists()) {
                return users;
            }

            Scanner scanner = new Scanner(USERS_FILE);
            StringBuilder jsonContent = new StringBuilder();
            while (scanner.hasNext()) {
                jsonContent.append(scanner.nextLine());
            }
            scanner.close();

            if (jsonContent.length() > 0) {
                JSONArray usersArray = new JSONArray(jsonContent.toString());
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userJSON = usersArray.getJSONObject(i);
                    String username = userJSON.getString("username");
                    String password = userJSON.getString("password");
                    String email = userJSON.getString("email");
                    String name = userJSON.getString("name");

                    User user = new User(username, password, email, name);
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Adds a new user to the JSON file.
     *
     * @param user The User object to be added to the storage.
     */
    public static void addUser(User user) {
        try {
            List<User> users = loadUsers();
            users.add(user);

            JSONArray usersArray = new JSONArray();
            for (User u : users) {
                JSONObject userJSON = new JSONObject();
                userJSON.put("username", u.getUsernameValue());
                userJSON.put("password", u.getPasswordValue());
                userJSON.put("email", u.getEmailValue());
                userJSON.put("name", u.getNameValue());

                usersArray.put(userJSON);
            }

            try (FileWriter writer = new FileWriter(USERS_FILE)) {
                writer.write(usersArray.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates the login credentials by checking if the username and password match any user.
     *
     * @param username The username to be validated.
     * @param password The password to be validated.
     * @return true if the credentials match a user, false otherwise.
     */
    public static boolean validateLogin(String username, String password) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsernameValue().equals(username) && user.getPasswordValue().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The User object if found, null if not found.
     */
    public static User getUserByUsername(String username) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsernameValue().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Updates the information of an existing user in the JSON file.
     *
     * @param updatedUser The User object with the updated information.
     */
    public static void updateUser(User updatedUser) {
        List<User> users = loadUsers();

        for (User user : users) {
            if (user.getUsernameValue().equals(updatedUser.getUsernameValue())) {
                user.setPasswordValue(updatedUser.getPasswordValue());  // Update password
                user.setEmailValue(updatedUser.getEmailValue());        // Update email
                user.setNameValue(updatedUser.getNameValue());          // Update name

                saveUsersToStorage(users);
                return;
            }
        }
        System.out.println("User not found: " + updatedUser.getUsernameValue());
    }

    /**
     * Saves the list of users to the JSON file.
     *
     * @param users The list of User objects to save.
     */
    public static void saveUsersToStorage(List<User> users) {
        try {
            JSONArray usersArray = new JSONArray();
            for (User u : users) {
                JSONObject userJSON = new JSONObject();
                userJSON.put("username", u.getUsernameValue());
                userJSON.put("password", u.getPasswordValue());
                userJSON.put("email", u.getEmailValue());
                userJSON.put("name", u.getNameValue());

                usersArray.put(userJSON);
            }

            try (FileWriter writer = new FileWriter(USERS_FILE)) {
                writer.write(usersArray.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
