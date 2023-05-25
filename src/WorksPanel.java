import listeners.ListActionPanelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WorksPanel extends JPanel implements ListActionPanelListener {

    DataSource workDataSource;

    JList list;

    public WorksPanel() {
        this.workDataSource = new DataSource<Work>("works.txt");
        ArrayList<Work> works = this.workDataSource.getListOfSourceObjects();
        Work[] listItemAbstracts = works.toArray(Work[]::new);
        this.list = new JList<>(listItemAbstracts);

        this.list.setCellRenderer(new CheckListWorkRenderer ());
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.addMouseListener(new WorkListMouseEvent());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        BasicActionList actionPanel = new BasicActionList(this);
        JScrollPane scrollPane = new JScrollPane(this.list);
        add(actionPanel);
        add(scrollPane);
        setVisible(true);
    }

    @Override
    public void delete() {
        ListModel<Work> model = list.getModel();
        int size = model.getSize();

        ArrayList<Work> remainingItems = new ArrayList<>();

        ArrayList<Work> updatedWorks = (ArrayList<Work>) IntStream.range(0, size)
                .mapToObj(model::getElementAt)
                .filter(item -> !item.isSelected())
                .peek(remainingItems::add)
                .map(item -> (Work) item)
                .collect(Collectors.toList());

        this.workDataSource.updateListOfUpdate(updatedWorks);

        DefaultListModel<Work> newModel = new DefaultListModel<>();
        remainingItems.forEach(newModel::addElement);
        list.setModel(newModel);
    }

    @Override
    public void update() {

        ListModel<Work> model = this.list.getModel();

        ArrayList<Work> checkedWorks = new ArrayList<>();

        // Checking if more than more item is checked
        for (int i = 0; i < model.getSize(); i++) {
            if(model.getElementAt(i).isSelected()) {
                checkedWorks.add(model.getElementAt(i));
            }
        }

    if (checkedWorks.size() == 1) {
            Work checkedWork = (Work) checkedWorks.get(0);

            EventQueue.invokeLater(() -> {
                JFrame updateWindow = new JFrame("Aktualizacja pracy");
                updateWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setVisible(true);

                JLabel workTypeLabel = new JLabel("Work Type: ");
                JComboBox<WorkType> workTypeJComboBox = new JComboBox<>(WorkType.values());

                JLabel timeLabel = new JLabel("Time: ");
                JSpinner timeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

                JLabel isDoneLabel = new JLabel("Is done");
                JCheckBox isDoneCheckbox = new JCheckBox("is Done");

                JLabel descriptionLabel = new JLabel("Description");
                JTextField descriptionField = new JTextField();

                JButton editButton = new JButton("Edit");

                // Settings values for field from user object

                workTypeJComboBox.setSelectedItem(checkedWork.getWorkType());
                timeSpinner.setValue(checkedWork.getTime());
                isDoneCheckbox.setSelected(checkedWork.isDone());
                descriptionField.setText(checkedWork.getDescription());

                panel.add(workTypeLabel);
                panel.add(workTypeJComboBox);
                panel.add(timeLabel);
                panel.add(timeSpinner);
                panel.add(isDoneLabel);
                panel.add(isDoneCheckbox);
                panel.add(descriptionLabel);
                panel.add(descriptionField);
                panel.add(editButton);

                editButton.addActionListener(e -> {

                    WorkType workType = (WorkType) workTypeJComboBox.getSelectedItem();
                    int timeSpinnerValue = (Integer) timeSpinner.getValue();
                    String descriptionValue = descriptionField.getText();
                    boolean isDoneValue = isDoneCheckbox.isSelected();

                    ArrayList<Work> works = this.workDataSource.getListOfSourceObjects();

                    ArrayList<Work> filteredWorks = (ArrayList<Work>) works.stream()
                                    .filter(work -> !work.getDescription().equals(checkedWork.getDescription()))
                                            .collect(Collectors.toList());

                    checkedWork.setDone(isDoneValue);
                    checkedWork.setWorkType(workType);
                    checkedWork.setDescription(descriptionValue);
                    checkedWork.setTime(timeSpinnerValue);

                    filteredWorks.add(checkedWork);

                    DefaultListModel<Work> newModel = new DefaultListModel<>();

                    for (Work filteredWork : filteredWorks) {
                        newModel.addElement((Work) filteredWork);
                    }

                    this.list.setModel(newModel);

                    this.workDataSource.updateListOfUpdate(filteredWorks);

                    JOptionPane.showMessageDialog(this, "Zaktualizowano Work'a", "Powiadomienie", JOptionPane.INFORMATION_MESSAGE);

                    updateWindow.dispose();


                });

                updateWindow.getContentPane().add(panel);
                updateWindow.pack();
                updateWindow.setLocationRelativeTo(null);
                updateWindow.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, "Zaznacz jeden element!", "Powiadomienie", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    @Override
    public void add() {
        EventQueue.invokeLater(() -> {
            JFrame addWindow = new JFrame("Dodanie pracy");
            ListModel currentModel = this.list.getModel();

            addWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setVisible(true);

            JLabel workTypeLabel = new JLabel("Work Type: ");
            JComboBox<WorkType> workTypeJComboBox = new JComboBox<>(WorkType.values());

            JLabel timeLabel = new JLabel("Time: ");
            JSpinner timeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

            JLabel descriptionLabel = new JLabel("Description");
            JTextField descriptionField = new JTextField();

            JButton addButton = new JButton("Create");

            panel.add(workTypeLabel);
            panel.add(workTypeJComboBox);

            panel.add(timeLabel);
            panel.add(timeSpinner);

            panel.add(descriptionLabel);
            panel.add(descriptionField);

            panel.add(addButton);

            addButton.addActionListener(e -> {
                WorkType workType = (WorkType) workTypeJComboBox.getSelectedItem();
                int timeSpinnerValue = (Integer) timeSpinner.getValue();
                String descriptionValue = descriptionField.getText();

                Work createdWork = Work.createWork(workType, timeSpinnerValue, descriptionValue);
                System.out.println(createdWork);

                workDataSource.saveObject(createdWork);

                DefaultListModel<Work> newModel = new DefaultListModel<>();

                for (int i = 0; i < currentModel.getSize(); i++) {
                    newModel.addElement((Work) currentModel.getElementAt(i));
                }

                newModel.addElement(createdWork);

                this.list.setModel(newModel);

                JOptionPane.showMessageDialog(addWindow, "Dodano nowa pracę " + createdWork.toString());

                addWindow.dispose();
            });

            addWindow.getContentPane().add(panel);
            addWindow.pack();
            addWindow.setLocationRelativeTo(null);
            addWindow.setVisible(true);


        });
    }
}

class WorkListMouseEvent extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent event) {
        JList list = (JList) event.getSource();
        int index = list.locationToIndex(event.getPoint());
        Work item = (Work) list.getModel()
                .getElementAt(index);
        item.setSelected(!item.isSelected());
        list.repaint(list.getCellBounds(index, index));
    }
}

class CheckListWorkRenderer extends JCheckBox implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((Work) value).isSelected());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setText(value.toString());
        return this;
    }
}