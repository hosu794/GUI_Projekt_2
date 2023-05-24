import listeners.EmployeesDepartmentListActionPanelListener;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeesDepartmentPanel extends JPanel implements EmployeesDepartmentListActionPanelListener {

    DataSource departmentDataSource;
    DataSource userDataSource;
    DataSource foremanDataSource;
    DataSource employeeDataSource;
    JList list;

    public EmployeesDepartmentPanel() {
        this.departmentDataSource = new DataSource<EmployeesDepartment>("departments.txt");
        this.userDataSource = new DataSource("users.txt");
        this.foremanDataSource = new DataSource<Foreman>("foremans.txt");

        this.employeeDataSource = new DataSource("employees.txt");
        ArrayList<EmployeesDepartment> departments = this.departmentDataSource.getListOfSourceObjects();

        CheckListItemAbstract[] listItemAbstracts = departments.toArray(CheckListItemAbstract[]::new);
        this.list = new JList<>(listItemAbstracts);
        this.list.setCellRenderer(new CheckListRenderer());
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.addMouseListener(new CheckListMouseEvent());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        DepartmentEmployeeBasicActionList actionPanel = new DepartmentEmployeeBasicActionList(this);
        JScrollPane scrollPane = new JScrollPane(this.list);
        add(actionPanel);
        add(scrollPane);
        setVisible(true);
    }

    @Override
    public void delete() {
        ListModel<CheckListItemAbstract> model = list.getModel();
        int size = model.getSize();

        ArrayList<CheckListItemAbstract> departmentsToDelete = new ArrayList<>();

        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).isSelected()) {
                departmentsToDelete.add(model.getElementAt(i));
            }
        }

        int numberOfUse = 0;

        for (int i = 0; i < departmentsToDelete.size(); i++) {

            EmployeesDepartment department = (EmployeesDepartment) departmentsToDelete.get(i);
            String departmentName = department.getName();

            ArrayList<Employee> allEmployees = this.employeeDataSource.getListOfSourceObjects();

            int numberOfUsedDepartmentInEmployees = (int) allEmployees.stream()
                    .filter(employee -> employee.getEmployeesDepartment().getName().equals(departmentName)).count();
            numberOfUse = numberOfUse + numberOfUsedDepartmentInEmployees;

            ArrayList<User> allUsers = this.userDataSource.getListOfSourceObjects();

            int numberOfUsedDepartmentInUsers = (int) allUsers.stream()
                    .filter(user -> user.getEmployeesDepartment().getName().equals(departmentName)).count();

            numberOfUse = numberOfUse + numberOfUsedDepartmentInUsers;

            ArrayList<Foreman> allForemen = this.foremanDataSource.getListOfSourceObjects();

            int numberOfUsedDepartmentInForeman = (int) allForemen.stream()
                    .filter(foreman -> foreman.getEmployeesDepartment().getName().equals(departmentName)).count();

            numberOfUse = numberOfUse + numberOfUsedDepartmentInForeman;
        }

        if (numberOfUse != 0) {
            JOptionPane.showMessageDialog(this, "Istnieje obiekt, który uzywa tego departmanetu",
                    "Bład usuwania", JOptionPane.ERROR_MESSAGE);
        } else {

        }


        //Sprawdzenie czy jakis Pracownik lub Uzytkownik jest przypisany do tej brygady.
    }

    @Override
    public void update() {

    }

    @Override
    public void add() {
            EventQueue.invokeLater(() -> {

                JFrame addWindow = new JFrame("Dodanie działow");
                addWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setVisible(true);

                JLabel nameLabel = new JLabel("Name: ");
                JTextField nameField = new JTextField(20);

                JButton createButton = new JButton("Dodaj");

                panel.add(nameLabel);
                panel.add(nameField);
                panel.add(createButton);

                createButton.addActionListener(e -> {
                    String name = nameField.getText();

                    ArrayList<EmployeesDepartment> allDepartments = this.departmentDataSource.getListOfSourceObjects();

                    Optional<EmployeesDepartment> foundDepartment = allDepartments.stream().filter(employeesDepartment -> employeesDepartment.getName().equals(name)).findFirst();

                    if (foundDepartment.isPresent()) {
                        JOptionPane.showMessageDialog(this, "Department z taką nazwą juz istnieje!",
                                "Błąd tworzenia", JOptionPane.ERROR_MESSAGE);
                    } else {
                        EmployeesDepartment newEmployeesDepartment = new EmployeesDepartment(name);

                        System.out.println(newEmployeesDepartment);

                        this.departmentDataSource.saveObject(newEmployeesDepartment);
                        ListModel currentModel = this.list.getModel();

                        DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();

                        for (int i = 0; i < currentModel.getSize(); i++) {
                            newModel.addElement((CheckListItemAbstract) currentModel.getElementAt(i));
                        }

                        newModel.addElement(newEmployeesDepartment);

                        this.list.setModel(newModel);

                        JOptionPane.showMessageDialog(addWindow, "Dodano nowy department");

                        addWindow.dispose();
                    }
                });

                addWindow.getContentPane().add(panel);
                addWindow.pack();
                addWindow.setLocationRelativeTo(null);
                addWindow.setVisible(true);
            });
    }

    @Override
    public void departmentEmployees() {
        ListModel<CheckListItemAbstract> model = list.getModel();
        ArrayList<CheckListItemAbstract> checkedDepartment = new ArrayList<>();

        for (int i = 0; i < model.getSize(); i++) {
            if(model.getElementAt(i).isSelected()) {
                checkedDepartment.add(model.getElementAt(i));
            }
        }

        if (checkedDepartment.size() == 1) {
            EmployeesDepartment employeesDepartment = (EmployeesDepartment)  checkedDepartment.get(0);

            EventQueue.invokeLater(() -> {
                JFrame displayDepartmentUsers = new JFrame("Pracownicy " + employeesDepartment.getName());
                displayDepartmentUsers.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                displayDepartmentUsers.add(panel);
                displayDepartmentUsers.setLayout(new FlowLayout());
                displayDepartmentUsers.setVisible(true);

                // Create list
                DataSource<Employee> employeeDataSource = new DataSource<Employee>("employees.txt");

                ArrayList<Employee> allEmployees = employeeDataSource.getListOfSourceObjects();

                ArrayList<Employee> departmentEmployee = (ArrayList<Employee>) allEmployees.stream()
                        .filter(employee -> employee.getEmployeesDepartment().getName()
                                .equals(employeesDepartment.getName())).collect(Collectors.toList());

                Employee[] employeesArray = departmentEmployee.toArray(Employee[]::new);

                if (employeesArray.length == 0) {
                    JLabel emptyDepartmentLabel = new JLabel("Brak pracowników w dziale");
                    panel.add(emptyDepartmentLabel);
                } else {
                    JList employeesList = new JList(employeesArray);
                    employeesList.setCellRenderer(new EmployeeListRenderer());
                    employeesList.addMouseListener(new EmployeeListMouseListener());
                    employeesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    JScrollPane scrollPane = new JScrollPane(employeesList);
                    panel.add(scrollPane);
                }

                panel.setVisible(true);

                displayDepartmentUsers.getContentPane().add(panel);
                displayDepartmentUsers.pack();
                displayDepartmentUsers.setLocationRelativeTo(null);
                displayDepartmentUsers.setVisible(true);

            });

        } else {
            JOptionPane.showMessageDialog(this, "Zaznacz jeden element!", "Powiadomienie"
                    , JOptionPane.INFORMATION_MESSAGE);

        }
    }
}

class EmployeeListRenderer implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            JLabel label = new JLabel();
            label.setOpaque(true);
            if (value instanceof Employee) {
                Employee employee = (Employee) value;
                label.setText(employee.getName() + " " + employee.getSurname());
            } else {
                label.setText(value.toString());
            }
            return label;
        }

        return null;
    }
}

class EmployeeListMouseListener extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            JList<Employee> list = (JList<Employee>) e.getSource();
            Employee selectedEmployee = list.getSelectedValue();

            System.out.println(selectedEmployee);

            if (selectedEmployee != null) {
                showEmployeeInfoDialog(selectedEmployee);
            }
        }
    }

    private void showEmployeeInfoDialog(Employee employee) {

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Imię: " + employee.getName()));
        panel.add(new JLabel("Nazwisko: " + employee.getSurname()));
        panel.add(new JLabel("Data urodzenia: " + employee.getBirthDate().toString()));
        panel.add(new JLabel("Department: " + employee.getEmployeesDepartment().getName()));


        JOptionPane.showOptionDialog(
                null,
                panel,
                "Informacje o pracowniku",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{},
                null
        );    }
}
