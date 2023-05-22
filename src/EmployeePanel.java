import controlls.CalendarControl;
import listeners.ListActionPanelListener;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmployeePanel extends JPanel implements ListActionPanelListener {

    DataSource employeeDataSource;

    JList list;

    DataSource departmentDataSource;

    ArrayList<EmployeesDepartment> departments;

    String[] departmentNamesArray;

    public EmployeePanel() {
        this.employeeDataSource = new DataSource<Employee>("employees.txt");
        this.departmentDataSource = new DataSource<EmployeesDepartment>("departments.txt");

        this.departments = this.departmentDataSource.getListOfSourceObjects();
        this.departmentNamesArray = departments.stream()
                .map(EmployeesDepartment::getName)
                .toArray(String[]::new);

        ArrayList<Employee> employees = this.employeeDataSource.getListOfSourceObjects();
        CheckListItemAbstract[] listItemAbstracts = employees.toArray(CheckListItemAbstract[]::new);
        this.list = new JList(listItemAbstracts);

        this.list.setCellRenderer(new CheckListRenderer());
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.addMouseListener(new CheckListMouseEvent());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        BasicActionList actionPanel = new BasicActionList(this);
        JScrollPane scrollPane = new JScrollPane(this.list);
        add(actionPanel);
        add(scrollPane);
        setVisible(true);
    }

    @Override
    public void delete() {
        ListModel<CheckListItemAbstract> model = list.getModel();
        int size = model.getSize();

        ArrayList<CheckListItemAbstract> remainingItems = new ArrayList<>();

        ArrayList<Employee> updatedEmployees = (ArrayList<Employee>) IntStream.range(0, size)
                .mapToObj(model::getElementAt)
                .filter(item -> !item.isSelected())
                .peek(remainingItems::add)
                .map(item -> (Employee) item)
                .collect(Collectors.toList());

        this.employeeDataSource.updateListOfUpdate(updatedEmployees);

        DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();
        remainingItems.forEach(newModel::addElement);
        list.setModel(newModel);
    }

    @Override
    public void update() {
        ListModel<CheckListItemAbstract> model = list.getModel();
        ArrayList<CheckListItemAbstract> checkedEmployees = new ArrayList<>();

        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).isSelected()) {
                checkedEmployees.add(model.getElementAt(i));
            }
        }

        if (checkedEmployees.size() == 1) {

            Employee checkedEmployee = (Employee) checkedEmployees.get(0);

            EventQueue.invokeLater(() -> {

                JFrame updateWindow = new JFrame("Update Employee");
                updateWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                JLabel nameLabel = new JLabel("Name: ");
                JTextField nameField = new JTextField(20);

                JLabel surnameLabel = new JLabel("Surname");
                JTextField surnameField = new JTextField(20);

                JLabel birthdayLabel = new JLabel("Birthday");
                CalendarControl calendarControl = new CalendarControl();

                JLabel departmentLabel = new JLabel("Department");
                JComboBox<String> departmentCombo = new JComboBox<>(this.departmentNamesArray);

                JButton editButton = new JButton("Update");

                updateWindow.add(panel);
                updateWindow.setLayout(new FlowLayout());
                updateWindow.setVisible(true);

                nameField.setText(checkedEmployee.getName());
                surnameField.setText(checkedEmployee.getSurname());
                calendarControl.setDate(checkedEmployee.getBirthDate());
                departmentCombo.setSelectedItem(checkedEmployee.getEmployeesDepartment().getName());

                panel.add(nameLabel);
                panel.add(nameField);
                panel.add(surnameLabel);
                panel.add(surnameField);
                panel.add(birthdayLabel);
                panel.add(calendarControl);
                panel.add(departmentLabel);
                panel.add(departmentCombo);
                panel.add(editButton);

                editButton.addActionListener(e -> {
                    LocalDate selectedDate = calendarControl.getSelectedDate();
                    String name = nameField.getText();
                    String surname = surnameField.getText();
                    String departmentName = (String) departmentCombo.getSelectedItem();

                    ArrayList<EmployeesDepartment> allDepartments =
                            (ArrayList<EmployeesDepartment>) this.departmentDataSource.getListOfSourceObjects();

                    EmployeesDepartment foundDepartment = allDepartments.stream().filter(employeesDepartment -> employeesDepartment.getName().equals(departmentName)).findAny().get();

                    ArrayList<Employee> employees = this.employeeDataSource.getListOfSourceObjects();

                    ArrayList<Employee> filteredEmployees = (ArrayList<Employee>) employees.stream()
                            .filter(user -> !user.getName()
                                    .equals(checkedEmployee.getName()) && !user.getSurname().equals(checkedEmployee.getSurname()))
                            .collect(Collectors.toList());


                    Employee updatedEmployee = new Employee(name, surname, selectedDate, foundDepartment);

                    filteredEmployees.add(updatedEmployee);

                    DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();

                    for (int i = 0; i < filteredEmployees.size(); i++) {
                        newModel.addElement((CheckListItemAbstract) filteredEmployees.get(i));
                    }

                    this.list.setModel(newModel);

                    this.employeeDataSource.updateListOfUpdate(filteredEmployees);

                    JOptionPane.showMessageDialog(this, "Zaktualizowano User'a", "Powiadomienie", JOptionPane.INFORMATION_MESSAGE);

                    updateWindow.dispose();
                });

                updateWindow.getContentPane().add(panel);
                updateWindow.pack();
                updateWindow.setLocationRelativeTo(null);
                updateWindow.setVisible(true);

            });

        } else {
            JOptionPane.showMessageDialog(this, "Check only one element!", "Notification", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    @Override
    public void add() {
        EventQueue.invokeLater(() -> {
            JFrame addWindow = new JFrame("Dodawanie pracownika");
            addWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setVisible(true);

            JLabel nameLabel = new JLabel("Name");
            JTextField nameField = new JTextField(20);

            JLabel surnameLabel = new JLabel("Surname");
            JTextField surnameField = new JTextField(20);

            JLabel birthDateLabel = new JLabel("Birthday");
            CalendarControl birthDateField = new CalendarControl();

            JLabel departmentLabel = new JLabel("Department");
            JComboBox<String> departmentCombo = new JComboBox<>(departmentNamesArray);

            JButton createButton = new JButton("Create");

            panel.add(nameLabel);
            panel.add(nameField);

            panel.add(surnameLabel);
            panel.add(surnameField);

            panel.add(birthDateLabel);
            panel.add(birthDateField);

            panel.add(departmentLabel);
            panel.add(departmentCombo);

            panel.add(createButton);

            createButton.addActionListener(e -> {
                LocalDate selectedDate = birthDateField.getSelectedDate();
                String name = nameField.getText();
                String surname = surnameField.getText();

                String departmentName = (String) departmentCombo.getSelectedItem();

                ArrayList<EmployeesDepartment> allDepartments =  (ArrayList<EmployeesDepartment>) this.departmentDataSource
                        .getListOfSourceObjects();
                EmployeesDepartment foundDepartment = allDepartments.stream().filter(employeesDepartment -> employeesDepartment.getName().equals(departmentName)).findAny().get();

                Employee newEmployee = new Employee(name, surname, selectedDate, foundDepartment);

                System.out.println(newEmployee);

                this.employeeDataSource.saveObject(newEmployee);

                ListModel currentModel = this.list.getModel();

                DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();

                for (int i = 0; i < currentModel.getSize(); i++) {
                    newModel.addElement((CheckListItemAbstract) currentModel.getElementAt(i));
                }

                newModel.addElement(newEmployee);

                this.list.setModel(newModel);

                JOptionPane.showMessageDialog(addWindow, "Dodano nowego Pracownika: " + newEmployee.toString());

                addWindow.dispose();
            });

            addWindow.getContentPane().add(panel);
            addWindow.pack();
            addWindow.setLocationRelativeTo(null);
            addWindow.setVisible(true);
        });
    }
}
