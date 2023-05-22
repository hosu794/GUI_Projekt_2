
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public class EmployeesDepartment extends CheckListItemAbstract implements Serializable {

    private static final long serialVersionUID = 3L;
    static public LinkedList<String> departmentsNames = new LinkedList<String>();

    private static long currentId = 1;
    private long id;

    private String name;
    public EmployeesDepartment(String name) {
        this.name = name;
        this.id = currentId;
        currentId++;
    }

    public static EmployeesDepartment createDepartment(String name) throws NotUniqueNameException {

        int results = (int) departmentsNames.stream().filter(s -> s.equals(name)).count();

        if (results > 0)  {
            throw new NotUniqueNameException("Department with name " + name + " already exists!"); }
        else {
            departmentsNames.add(name);
            return new EmployeesDepartment(name);
        }

    }

    public void addBrigade(Brigade brigade) {
        this.addBrigade(brigade);
    }

    public ArrayList<Employee> getAllEmployess() {
        return (ArrayList<Employee>) Employee.employees.stream().filter(employee -> Objects.equals(employee.getEmployeesDepartment().getName(), this.getName())).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "EmployeesDepartment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
