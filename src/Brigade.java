import java.io.Serializable;
import java.util.ArrayList;

public class Brigade extends CheckListItemAbstract implements Serializable {

    private static final long serialVersionUID = 6L;

    private String name;
    private Foreman foreman;

    private long id;

    private static long currentId = 1;

    private ArrayList<Employee> employeeList;

    public Brigade(String name, Foreman foreman) {
        foreman.getBrigades().put(this, new ArrayList<>());
        this.name = name;
        this.foreman = foreman;
        this.employeeList = new ArrayList<>();
        this.id = currentId;
        currentId++;
    }

    public void addWorker(Employee employee) {
        this.employeeList.add(employee);
    }

    public void addWorker(ArrayList<Employee> employees) {
        this.employeeList.addAll(employees);
    }

    public ArrayList<Employee> getEmployeeList() {
        return employeeList;
    }

    public Foreman getForeman() {
        return foreman;
    }

    @Override
    public String toString() {
        return "Brigade{" +
                "name='" + name + '\'' +
                ", foreman=" + foreman +
                ", id=" + id +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setForeman(Foreman foreman) {
        this.foreman = foreman;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static long getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(long currentId) {
        Brigade.currentId = currentId;
    }

    public void setEmployeeList(ArrayList<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}
