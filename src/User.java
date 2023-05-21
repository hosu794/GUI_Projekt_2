import java.io.Serializable;
import java.time.LocalDate;


public class User extends Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int FIRST_LETTER_INDEX = 0;
    private String login;
    public String password;
    private String initial;

    private static long currentId = 1;

    private long id;

    static User createUser(String name, String surname, LocalDate birthDate, String login, String password, EmployeesDepartment employeesDepartment) {
        User user = new User(name, surname, birthDate, login, password, employeesDepartment);

        employees.add(user);
        return user;
    }
    public User(String name, String surname, LocalDate birthDate, String login, String password, EmployeesDepartment employeesDepartment) {
        super(name, surname, birthDate, employeesDepartment);
        this.login = login;
        this.password = password;

        this.createInitial(name, surname);

        this.id = currentId;
        currentId++;
    }

    private void createInitial(String name, String surname) {
        String firstNameLetter = String.valueOf(name.charAt(FIRST_LETTER_INDEX));
        String firstSurnameLetter = String.valueOf(surname.charAt(FIRST_LETTER_INDEX));

        this.initial = firstNameLetter + firstSurnameLetter;
    }

    public void setName(String name) {

        this.name = name;

        String firstNameLetter = String.valueOf(name.charAt(FIRST_LETTER_INDEX));

        String currentSurname = this.surname;

        String firstSurnameLetter = String.valueOf(currentSurname.charAt(FIRST_LETTER_INDEX));

        this.initial = firstNameLetter + firstSurnameLetter;
    }

    public void setSurname(String surname) {
        this.surname = surname;

        String firstNameLetter = String.valueOf(this.name.charAt(FIRST_LETTER_INDEX));
        String firstSurnameLetter = String.valueOf(surname.charAt(FIRST_LETTER_INDEX));

        this.initial = firstNameLetter + firstSurnameLetter;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", initial='" + initial + '\'' +
                ", id=" + id +
                '}';
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }
}
