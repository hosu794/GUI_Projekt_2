import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.LocalDate;


public abstract class Employee implements Comparable<Employee> {

    static List<Employee> employees = new ArrayList<>();

    protected String name;
    protected String surname;
    protected LocalDate birthDate;
    private EmployeesDepartment employeesDepartment;
    public Employee(String name, String surname, LocalDate birthDate, EmployeesDepartment employeesDepartment) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.employeesDepartment = employeesDepartment;
    }

    public static List<Employee> getEmployees() {
        return employees;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    // accessor methods
    @Override
    public int compareTo(Employee o) {
        return Comparator.comparing(Employee::getName)
                .thenComparing(Employee::getSurname)
                .thenComparing(Employee::getBirthDate)
                .compare(this, o);
    }

    public EmployeesDepartment getEmployeesDepartment() {
        return employeesDepartment;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", employeesDepartment=" + employeesDepartment +
                '}';
    }
}
