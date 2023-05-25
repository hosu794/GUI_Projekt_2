import controlls.CalendarControl;
import listeners.ListActionPanelListener;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UsersPanel extends JPanel implements ListActionPanelListener {

    DataSource userDataSource;
    JList list;
    DataSource departmentDataSource;

    public UsersPanel() {

        this.userDataSource = new DataSource<User>("users.txt");
        this.departmentDataSource = new DataSource<EmployeesDepartment>("departments.txt");



        ArrayList<User> users = this.userDataSource.getListOfSourceObjects();
        CheckListItemAbstract[] listItemAbstracts = users.toArray(CheckListItemAbstract[]::new);
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

        ArrayList<User> updatedUsers = (ArrayList<User>) IntStream.range(0, size)
                .mapToObj(model::getElementAt)
                .filter(item -> !item.isSelected())
                .peek(remainingItems::add)
                .map(item -> (User) item)
                .collect(Collectors.toList());

        this.userDataSource.updateListOfUpdate(updatedUsers);

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

            ArrayList<CheckListItemAbstract> checkedUsers = new ArrayList<>();

            // Checking if more than more item is checked
            for (int i = 0; i < model.getSize(); i++) {
                if(model.getElementAt(i).isSelected()) {
                    checkedUsers.add(model.getElementAt(i));
                }
            }

            if (checkedUsers.size() == 1) {

                User checkedUser = (User) checkedUsers.get(0);

                User currentUser = LoggedInUser.getInstance().getUser();

                if (currentUser.getLogin().equals(checkedUser.getLogin())) {
                    JOptionPane.showMessageDialog(this, "Nie mozesz edytować zalogowanego obecnie uzytkownika!", "Powiadomienie", JOptionPane.ERROR_MESSAGE);
                } else {
                    EventQueue.invokeLater(() -> {

                        JFrame updateWindow = new JFrame("Aktualizacja uzytkownika");
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

                        nameField.setText(checkedUser.getName());
                        surnameField.setText(checkedUser.getSurname());
                        calendarControl.setDate(checkedUser.getBirthDate());
                        loginField.setText(checkedUser.getLogin());
                        passwordField.setText(checkedUser.getPassword());

                        departmentCombo.setSelectedItem(checkedUser.getEmployeesDepartment().getName());

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

                            ArrayList<User> users = this.userDataSource.getListOfSourceObjects();

                            ArrayList<User> filteredUsers = (ArrayList<User>) users.stream()
                                    .filter(user -> !user.getLogin().equals(checkedUser.getLogin())).collect(Collectors.toList());

                            User updatedUser = User.createUser(name, surname, selectedDate, login, password, foundDepartment);

                            filteredUsers.add(updatedUser);

                            DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();

                            for (User filteredUser : filteredUsers) {
                                newModel.addElement((CheckListItemAbstract) filteredUser);
                            }

                            this.list.setModel(newModel);

                            this.userDataSource.updateListOfUpdate(filteredUsers);

                            JOptionPane.showMessageDialog(this, "Zaktualizowano User'a", "Powiadomienie", JOptionPane.INFORMATION_MESSAGE);

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

                JFrame addWindow = new JFrame("Dodawanie uzytkownika");
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

                    ArrayList<User> allUsers = this.userDataSource.getListOfSourceObjects();

                    Optional<User> foundUser = allUsers.stream().filter(user -> user.getLogin().equals(login)).findAny();

                    if (foundUser.isPresent()) {
                        JOptionPane.showMessageDialog(addWindow, "User z login=" + login + ", istnieje!");
                    } else {
                        User createdUser = User.createUser(name, surname, selectedDate, login, password, foundDepartment);

                        System.out.println(createdUser);

                        userDataSource.saveObject(createdUser);
                        ListModel currentModel = this.list.getModel();

                        DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();

                        for (int i = 0; i < currentModel.getSize(); i++) {
                            newModel.addElement((CheckListItemAbstract) currentModel.getElementAt(i));
                        }

                        newModel.addElement(createdUser);

                        this.list.setModel(newModel);

                        JOptionPane.showMessageDialog(addWindow, "Dodano nowego Uzytkownika: " + createdUser.toString());

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

class CheckListRenderer extends JCheckBox implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((CheckListItemAbstract) value).isSelected());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setText(value.toString());
        return this;
    }
}

