package hi.event.vinnsla;

/******************************************************************************
 *  Author    : Ásdís Halldóra L Stefánsdóttir
 *  Email: ahl4@hi.is
 *
 *  Description  : This class manages the session for the logged-in user. It provides static methods
 *  to set, get, and clear the logged-in user.
 *  The `loggedInUser` is stored as a static field, allowing it to be accessed globally
 *  within the application.
 *
 *
 *****************************************************************************/

public class UserSession {
    private static User loggedInUser;

    /**
     * Sets the user as the logged-in user for the current session.
     *
     * @param user The user object representing the logged-in user.
     */
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    /**
     * Returns the current logged-in user.
     *
     * @return The currently logged-in user, or null if no user is logged in.
     */
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Clears the currently logged-in user, effectively logging out the user.
     * This sets the logged-in user to null.
     */
    public static void clearLoggedInUser() {
        loggedInUser = null;
    }
}
