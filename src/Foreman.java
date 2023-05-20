import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Foreman extends User implements Serializable {

    private static final long serialVersionUID = 5L;

    private HashMap<Brigade, ArrayList<Job>> brigades;
    private static long currentId = 1;
    private long id;
    private ArrayList<Job> jobs;
    private ArrayList<Brigade> brigadeList;
    public static Foreman createForeman(String name, String surname, LocalDate birthdate, String login, String password, EmployeesDepartment employeesDepartment) {
        Foreman foreman = new Foreman(name, surname, birthdate, login, password, employeesDepartment);
        employees.add(foreman);

        return foreman;
    }

    public HashMap<Brigade, ArrayList<Job>> getBrigades() {
        return brigades;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public Foreman(String name, String surname, LocalDate birthDate, String login, String password, EmployeesDepartment employeesDepartment) {
        super(name, surname, birthDate, login, password, employeesDepartment);
        this.brigades = new HashMap<>();
        this.jobs = new ArrayList<>();
        this.brigadeList = new ArrayList<>();
        this.id = currentId;
        currentId++;
    }

    @Override
    public String toString() {
        return "Foreman{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }

    public ArrayList<Brigade> getBrigadeList() {
        return brigadeList;
    }
}
