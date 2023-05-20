public class LoggedInUser {
    private static LoggedInUser instance;
    private User user;

    private LoggedInUser() {
        // Prywatny konstruktor, aby uniemożliwić tworzenie instancji z zewnątrz
    }

    public static LoggedInUser getInstance() {
        if (instance == null) {
            instance = new LoggedInUser();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
