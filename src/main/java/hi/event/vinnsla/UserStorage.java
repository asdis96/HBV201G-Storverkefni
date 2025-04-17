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
 *  Description  :
 *
 *
 *****************************************************************************/

public class UserStorage {

    private static final File USERS_FILE = new File("users.json");

    // Method to read all users from the JSON file
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try {
            if (!USERS_FILE.exists()) {
                return users;  // No users file, return empty list
            }

            // Read the file content
            Scanner scanner = new Scanner(USERS_FILE);
            StringBuilder jsonContent = new StringBuilder();
            while (scanner.hasNext()) {
                jsonContent.append(scanner.nextLine());
            }
            scanner.close();

            // Parse the content of the file into a JSONArray
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

    // Method to add a new user to the JSON file
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


    public static boolean validateLogin(String username, String password) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsernameValue().equals(username) && user.getPasswordValue().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Method to get a user by username
    public static User getUserByUsername(String username) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsernameValue().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // Method to update an existing user's information in the JSON file
    public static void updateUser(User updatedUser) {
        List<User> users = loadUsers();

        for (User user : users) {
            if (user.getUsernameValue().equals(updatedUser.getUsernameValue())) {
                // Update the user information
                user.setPasswordValue(updatedUser.getPasswordValue());  // Update password
                user.setEmailValue(updatedUser.getEmailValue());        // Update email
                user.setNameValue(updatedUser.getNameValue());          // Update name

                // Save the updated users list back to the file
                saveUsersToStorage(users);
                return;
            }
        }
        // If user not found, handle as needed (optional)
        System.out.println("User not found: " + updatedUser.getUsernameValue());
    }

    // Method to save the updated list of users back to the JSON file
    public static void saveUsersToStorage(List<User> users) {
        try {
            // Convert the users list to a JSONArray
            JSONArray usersArray = new JSONArray();
            for (User u : users) {
                JSONObject userJSON = new JSONObject();
                userJSON.put("username", u.getUsernameValue());
                userJSON.put("password", u.getPasswordValue());
                userJSON.put("email", u.getEmailValue());
                userJSON.put("name", u.getNameValue());

                usersArray.put(userJSON);
            }

            // Write the updated users list back to the file
            try (FileWriter writer = new FileWriter(USERS_FILE)) {
                writer.write(usersArray.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
