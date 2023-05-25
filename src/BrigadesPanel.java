import listeners.ListActionPanelListener;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BrigadesPanel extends JPanel implements ListActionPanelListener {

    DataSource brigadeDataSource;

    JList list;

    DataSource foremenDataSource;

    DataSource employeeDataSource;

    public BrigadesPanel() {
        this.brigadeDataSource = new DataSource<Brigade>("brigades.txt");
        this.foremenDataSource = new DataSource<Foreman>("foremans.txt");
        this.employeeDataSource = new DataSource<Employee>("employees.txt");

        ArrayList<Brigade> brigades = this.brigadeDataSource.getListOfSourceObjects();

        CheckListItemAbstract[] listItemAbstracts = brigades.toArray(CheckListItemAbstract[]::new);
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

        ArrayList<Brigade> updatedBrigades = (ArrayList<Brigade>) IntStream.range(0, size)
                .mapToObj(model::getElementAt)
                .filter(item -> !item.isSelected())
                .peek(remainingItems::add)
                .map(item -> (Brigade) item)
                .collect(Collectors.toList());

        this.brigadeDataSource.updateListOfUpdate(updatedBrigades);

        DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();
        remainingItems.forEach(newModel::addElement);
        list.setModel(newModel);
    }

    @Override
    public void update() {

    }

    @Override
    public void add() {
        EventQueue.invokeLater(() -> {

            ArrayList<Foreman> foremen = this.foremenDataSource.getListOfSourceObjects();
            String[] foremenNames = foremen.stream()
                    .map(Foreman::getLogin)
                    .toArray(String[]::new);

            ArrayList<Employee> allEmployees = this.employeeDataSource.getListOfSourceObjects();

            JFrame addWindow = new JFrame("Dodawanie brygady");
            addWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setVisible(true);

            JLabel nameLabel = new JLabel("Name: ");
            JTextField nameField = new JTextField(20);

            JLabel foremenLabel = new JLabel("Foremen: ");
            JComboBox<String> foremenCombo = new JComboBox<>(foremenNames);

            JLabel employeesLabel = new JLabel("Employees: ");
            CheckListItemAbstract[] listItemAbstracts = allEmployees.toArray(CheckListItemAbstract[]::new);
            JList employeesList = new JList(listItemAbstracts);

            employeesList.setCellRenderer(new CheckListRenderer());
            employeesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            employeesList.addMouseListener(new CheckListMouseEvent());

            JButton addButton = new JButton("Add");

            panel.add(nameLabel);
            panel.add(nameField);

            panel.add(foremenLabel);
            panel.add(foremenCombo);

            panel.add(employeesLabel);

            panel.add(employeesList);

            panel.add(addButton);

            addButton.addActionListener(e -> {
                String name = nameField.getText();
                String foremanLogin = (String) foremenCombo.getSelectedItem();

                ListModel<CheckListItemAbstract> model = employeesList.getModel();
                ArrayList<Employee> checkedEmployees = new ArrayList<>();

                for (int i = 0; i < model.getSize(); i++) {
                    if (model.getElementAt(i).isSelected()) checkedEmployees.add((Employee) model.getElementAt(i));
                }

                ArrayList<Foreman> allForemen = this.foremenDataSource.getListOfSourceObjects();
                Optional<Foreman> foundForeman = allForemen.stream()
                        .filter(f -> f.getLogin().equals(foremanLogin)).findFirst();

                if (foundForeman.isPresent()) {
                    Foreman foreman = foundForeman.get();

                    Brigade newBrigade = new Brigade(name, foreman);
                    newBrigade.addWorker(checkedEmployees);

                    this.brigadeDataSource.saveObject(newBrigade);

                    ListModel currentModel = this.list.getModel();

                    DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();

                    for (int i = 0; i < currentModel.getSize(); i++) {
                        newModel.addElement((CheckListItemAbstract) currentModel.getElementAt(i));
                    }

                    newModel.addElement(newBrigade);

                    this.list.setModel(newModel);

                    JOptionPane.showMessageDialog(addWindow, "Dodano nową brygadę:  " + newBrigade.toString());

                    addWindow.dispose();

                } else {
                    JOptionPane.showMessageDialog(this, "Nie znaleziono brygadzisty", "Błąd", JOptionPane.ERROR_MESSAGE);

                }
            });

            addWindow.getContentPane().add(panel);
            addWindow.pack();
            addWindow.setLocationRelativeTo(null);
            addWindow.setVisible(true);


        });
    }
}
