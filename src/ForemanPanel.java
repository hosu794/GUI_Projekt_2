import controlls.CalendarControl;
import listeners.ListActionPanelListener;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ForemanPanel extends JPanel implements ListActionPanelListener {

    DataSource foremenDataSource;

    JList list;

    DataSource departmentDataSource;

    public ForemanPanel() {

        this.foremenDataSource = new DataSource<Foreman>("foremans.txt");
        this.departmentDataSource = new DataSource<EmployeesDepartment>("departments.txt");

        ArrayList<Foreman> foremen = this.foremenDataSource.getListOfSourceObjects();
        CheckListItemAbstract[] listItemAbstracts = foremen.toArray(CheckListItemAbstract[]::new);
        this.list = new JList<>(listItemAbstracts);

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

        ArrayList<Foreman> updatedUsers = (ArrayList<Foreman>) IntStream.range(0, size)
                .mapToObj(model::getElementAt)
                .filter(item -> !item.isSelected())
                .peek(remainingItems::add)
                .map(item -> (Foreman) item)
                .collect(Collectors.toList());

        this.foremenDataSource.updateListOfUpdate(updatedUsers);

        DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();
        remainingItems.forEach(newModel::addElement);
        list.setModel(newModel);
    }

    @Override
    public void update() {
        ArrayList<EmployeesDepartment> departments = this.departmentDataSource.getListOfSourceObjects();
        String[] departmentNamesArray = departments.stream()
                .map(EmployeesDepartment::getName)
                .toArray(String[]::new);

        ListModel<CheckListItemAbstract> model = list.getModel();

        ArrayList<CheckListItemAbstract> checkedForemen = new ArrayList<>();

        // Checking if more than more item is checked
        for (int i = 0; i < model.getSize(); i++) {
            if(model.getElementAt(i).isSelected()) {
                checkedForemen.add(model.getElementAt(i));
            }
        }

        if (checkedForemen.size() == 1) {

            Foreman checkedForeman = (Foreman) checkedForemen.get(0);

            User currentUser = LoggedInUser.getInstance().getUser();

            if ((currentUser instanceof Foreman currentForeman) && (currentUser.getLogin().equals(checkedForeman.getLogin()))) {
                JOptionPane.showMessageDialog(this, "Nie mozesz edytować zalogowanego obecnie brygadzisty!", "Powiadomienie", JOptionPane.ERROR_MESSAGE);
            } else {
                EventQueue.invokeLater(() -> {

                    JFrame updateWindow = new JFrame("Aktualizacja brygadzisty");
                    updateWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                    updateWindow.add(panel);
                    updateWindow.setLayout(new FlowLayout());
                    updateWindow.setVisible(true);

                    // Initiation fields

                    JLabel nameLabel = new JLabel("Name: ");
                    JTextField nameField = new JTextField(20);
                    JLabel surnameLabel = new JLabel("Surname");
                    JTextField surnameField = new JTextField(20);
                    JLabel birthDateField = new JLabel("Date");
                    CalendarControl calendarControl = new CalendarControl();
                    JLabel loginLabel = new JLabel("Login");
                    JTextField loginField = new JTextField(20);
                    JLabel passwordLabel = new JLabel("Password");
                    JTextField passwordField = new JPasswordField(20);
                    JComboBox<String> departmentCombo = new JComboBox<>(departmentNamesArray);
                    JButton editButton = new JButton("Aktualizuj");


                    // Setting values to field from user object

                    nameField.setText(checkedForeman.getName());
                    surnameField.setText(checkedForeman.getSurname());
                    calendarControl.setDate(checkedForeman.getBirthDate());
                    loginField.setText(checkedForeman.getLogin());
                    passwordField.setText(checkedForeman.getPassword());

                    departmentCombo.setSelectedItem(checkedForeman.getEmployeesDepartment().getName());

                    panel.add(nameLabel);
                    panel.add(nameField);
                    panel.add(surnameField);
                    panel.add(surnameLabel);
                    panel.add(birthDateField);
                    panel.add(calendarControl);
                    panel.add(loginLabel);
                    panel.add(loginField);
                    panel.add(passwordLabel);
                    panel.add(passwordField);
                    panel.add(departmentCombo);
                    panel.add(editButton);

                    editButton.addActionListener(e -> {

                        LocalDate selectedDate = calendarControl.getSelectedDate();
                        String name = nameField.getText();
                        String surname = surnameField.getText();
                        String login = loginField.getText();
                        String password = passwordField.getText();
                        String departmentName = (String) departmentCombo.getSelectedItem();

                        ArrayList<EmployeesDepartment> allDepartments =
                                (ArrayList<EmployeesDepartment>) this.departmentDataSource.getListOfSourceObjects();

                        EmployeesDepartment foundDepartment = allDepartments.stream().filter(employeesDepartment -> employeesDepartment.getName().equals(departmentName)).findAny().get();

                        ArrayList<Foreman> users = this.foremenDataSource.getListOfSourceObjects();

                        ArrayList<Foreman> filteredForemen = (ArrayList<Foreman>) users.stream()
                                .filter(foreman -> !foreman.getLogin().equals(checkedForeman.getLogin())).collect(Collectors.toList());

                        Foreman updatedForeman = Foreman.createForeman(name, surname, selectedDate, login, password, foundDepartment);

                        filteredForemen.add(updatedForeman);

                        DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();

                        for (User filteredUser : filteredForemen) {
                            newModel.addElement((CheckListItemAbstract) filteredUser);
                        }

                        this.list.setModel(newModel);

                        this.foremenDataSource.updateListOfUpdate(filteredForemen);

                        JOptionPane.showMessageDialog(this, "Zaktualizowano Brygadziste", "Powiadomienie", JOptionPane.INFORMATION_MESSAGE);

                        updateWindow.dispose();

                    });

                    updateWindow.getContentPane().add(panel);
                    updateWindow.pack();
                    updateWindow.setLocationRelativeTo(null);
                    updateWindow.setVisible(true);

                });
            }

        } else {
            JOptionPane.showMessageDialog(this, "Zaznacz jeden element!", "Powiadomienie", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    @Override
    public void add() {
        EventQueue.invokeLater(() -> {

            ArrayList<EmployeesDepartment> departments = this.departmentDataSource.getListOfSourceObjects();
            String[] departmentNamesArray = departments.stream()
                    .map(EmployeesDepartment::getName)
                    .toArray(String[]::new);

            JFrame addWindow = new JFrame("Dodawanie brygadzisty");
            addWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setVisible(true);

            JLabel nameLabel = new JLabel("Name: ");
            JTextField nameField = new JTextField(20);
            JLabel surnameLabel = new JLabel("Surname");
            JTextField surnameField = new JTextField(20);
            JLabel birthDateLabel = new JLabel("Date");
            CalendarControl birthDateField = new CalendarControl();
            JLabel loginLabel = new JLabel("Login");
            JTextField loginField = new JTextField(20);
            JLabel passwordLabel = new JLabel("Password");
            JPasswordField passwordField = new JPasswordField(20);
            JComboBox<String> departmentCombo = new JComboBox<>(departmentNamesArray);

            panel.add(nameLabel);
            panel.add(nameField);
            panel.add(surnameLabel);
            panel.add(surnameField);
            panel.add(birthDateLabel);
            panel.add(birthDateField);
            panel.add(loginLabel);
            panel.add(loginField);
            panel.add(passwordLabel);
            panel.add(passwordField);
            panel.add(departmentCombo);
            JButton createButton = new JButton("Create");
            panel.add(createButton);

            createButton.addActionListener(e -> {
                LocalDate selectedDate = birthDateField.getSelectedDate();
                String name = nameField.getText();
                String surname = surnameField.getText();
                String login = loginField.getText();
                String password = new String(passwordField.getPassword());

                String departmentName = (String) departmentCombo.getSelectedItem();

                ArrayList<EmployeesDepartment> allDepartments = (ArrayList<EmployeesDepartment>) this.departmentDataSource.getListOfSourceObjects();

                EmployeesDepartment foundDepartment = allDepartments.stream().filter(employeesDepartment -> employeesDepartment.getName().equals(departmentName)).findAny().get();

                ArrayList<Foreman> allForemen = this.foremenDataSource.getListOfSourceObjects();

                Optional<Foreman> foundForeman = allForemen.stream().filter(foreman -> foreman.getLogin().equals(login)).findAny();

                if (foundForeman.isPresent()) {
                    JOptionPane.showMessageDialog(addWindow, "User z foremen=" + login + ", istnieje!");
                } else {
                    Foreman cretedForeman = Foreman.createForeman(name, surname, selectedDate, login, password, foundDepartment);

                    System.out.println(cretedForeman);

                    this.foremenDataSource.saveObject(cretedForeman);
                    ListModel currentModel = this.list.getModel();

                    DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();

                    for (int i = 0; i < currentModel.getSize(); i++) {
                        newModel.addElement((CheckListItemAbstract) currentModel.getElementAt(i));
                    }

                    newModel.addElement(cretedForeman);

                    this.list.setModel(newModel);

                    JOptionPane.showMessageDialog(addWindow, "Dodano nowego Brygadziste: " + cretedForeman.toString());

                    addWindow.dispose();
                }

            });

            addWindow.getContentPane().add(panel);
            addWindow.pack();
            addWindow.setLocationRelativeTo(null);
            addWindow.setVisible(true);
        });
    }
}
