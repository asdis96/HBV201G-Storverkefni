package hi.event.vinnsla;

public class UserSession {
    private static User loggedInUser;

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void clearLoggedInUser() {
        loggedInUser = null;
    }
}
