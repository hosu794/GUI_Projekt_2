public class CurrentUser {


    static private User currentUser;

    static public void setCurrentUser(User user) {
        currentUser = user;
    }

    static public void logout() {
        currentUser = null;
    }

    static public User get() {
        return currentUser;
    }


}
