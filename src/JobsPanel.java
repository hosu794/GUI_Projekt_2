import listeners.JobListActionPanelListener;
import listeners.ListActionPanelListener;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JobsPanel extends JPanel implements JobListActionPanelListener {

    DataSource jobDataSource;
    JList list;

    DataSource brigadeDataSource;

    DataSource worksDataSource;

    public JobsPanel() {
        this.jobDataSource = new DataSource<Brigade>("jobs.txt");
        this.brigadeDataSource = new DataSource<Foreman>("brigades.txt");
        this.worksDataSource = new DataSource<Employee>("works.txt");

        ArrayList<Job> jobs = this.jobDataSource.getListOfSourceObjects();

        CheckListItemAbstract[] listItemAbstracts = jobs.toArray(CheckListItemAbstract[]::new);
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

        ArrayList<Job> updatedBrigades = (ArrayList<Job>) IntStream.range(0, size)
                .mapToObj(model::getElementAt)
                .filter(item -> !item.isSelected())
                .peek(remainingItems::add)
                .map(item -> (Job) item)
                .collect(Collectors.toList());

        this.jobDataSource.updateListOfUpdate(updatedBrigades);

        DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();
        remainingItems.forEach(newModel::addElement);
        list.setModel(newModel);
    }

    @Override
    public void update() {
        ListModel<CheckListItemAbstract> model = this.list.getModel();

        ArrayList<CheckListItemAbstract> checkedJobs = new ArrayList<>();

        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).isSelected()) checkedJobs.add(model.getElementAt(i));
        }

        if (checkedJobs.size() == 1) {

            Job checkedJob = (Job) checkedJobs.get(0);

            JFrame updateFrame = new JFrame("Aktualizacja zlecenia");
            updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            updateFrame.add(panel);
            updateFrame.setLayout(new FlowLayout());
            updateFrame.setVisible(true);

            ArrayList<Brigade> brigades = this.brigadeDataSource.getListOfSourceObjects();
            String[] brigadesNames = brigades.stream()
                    .map(Brigade::getName)
                    .toArray(String[]::new);

            ArrayList<Work> allWorks = this.worksDataSource.getListOfSourceObjects();

            // Create component
            JLabel isPlannedLabel = new JLabel("Job status: ");
            JCheckBox isPlannedCheckbox = new JCheckBox();

            JLabel worksLabel = new JLabel("Works: ");
            Work[] listItemAbstracts = allWorks.toArray(Work[]::new);
            JList worksList = new JList(listItemAbstracts);

            JLabel brigadeLabel = new JLabel("Brigade Label: ");
            JComboBox<String> brigadeCombo = new JComboBox<>(brigadesNames);

            JButton editButton = new JButton("Edit");

            // Setting old values
            isPlannedCheckbox.setSelected((checkedJob.getStatus() == JobStatus.PLANNED));
            brigadeCombo.setSelectedItem(checkedJob.getBrigade().getName());

            worksList.setCellRenderer(new CheckListWorkRenderer());
            worksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            worksList.addMouseListener(new WorkListMouseEvent());

            DefaultListModel<Work> newModelFromJobWorks = new DefaultListModel<>();

            for (int j = 0; j < worksList.getModel().getSize(); j++) {

                Work work = (Work) worksList.getModel().getElementAt(j);

                Optional<Work> foundEmployee = checkedJob.getWorks().stream()
                        .filter(e -> e.getDescription().equals(work.getDescription()))
                        .findFirst();

                work.setSelected(foundEmployee.isPresent());
                newModelFromJobWorks.addElement(work);
            }

            worksList.setModel(newModelFromJobWorks);

            panel.add(brigadeLabel);
            panel.add(brigadeCombo);

            panel.add(worksLabel);
            panel.add(worksList);

            panel.add(isPlannedLabel);
            panel.add(isPlannedCheckbox);

            panel.add(editButton);

            editButton.addActionListener(e -> {
                String brigadeName = (String) brigadeCombo.getSelectedItem();
                boolean isPlanned = isPlannedCheckbox.isSelected();

                ListModel<Work> newWorksList = worksList.getModel();
                ArrayList<Work> checkedWorks = new ArrayList<>();

                for (int i = 0; i < newWorksList.getSize(); i++) {
                    if (newWorksList.getElementAt(i).isSelected()) checkedWorks.add(newWorksList.getElementAt(i));
                }

                ArrayList<Brigade> allBrigades = this.brigadeDataSource.getListOfSourceObjects();
                Optional<Brigade> foundBrigade =allBrigades.stream()
                        .filter(work -> work.getName().equals(brigadeName)).findFirst();

                if (foundBrigade.isPresent()) {
                    Brigade brigade = foundBrigade.get();

                    ArrayList<Job> allJobs = this.jobDataSource.getListOfSourceObjects();

                    ArrayList<Job> filteredJobs = (ArrayList<Job>) allJobs.stream()
                            .filter(job -> !job.getUuid().equals(checkedJob.getUuid())).collect(Collectors.toList());

                    Job newJob = Job.createJob(isPlanned, checkedWorks, brigade);

                    filteredJobs.add(newJob);

                    DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();

                    for (Job filteredJob : filteredJobs) {
                        newModel.addElement((CheckListItemAbstract) filteredJob);
                    }

                    this.list.setModel(newModel);

                    this.jobDataSource.updateListOfUpdate(filteredJobs);

                    JOptionPane.showMessageDialog(this, "Zaktualizowano Zlecenie", "Powiadomienie", JOptionPane.INFORMATION_MESSAGE);

                    updateFrame.dispose();

                }


            });

            updateFrame.getContentPane().add(panel);
            updateFrame.pack();
            updateFrame.setLocationRelativeTo(null);
            updateFrame.setVisible(true);

        }else {
            JOptionPane.showMessageDialog(this, "Zaznacz jeden element!", "Powiadomienie", JOptionPane.INFORMATION_MESSAGE);
        }


    }

    @Override
    public void add() {
        EventQueue.invokeLater(() -> {
            ArrayList<Brigade> brigades = this.brigadeDataSource.getListOfSourceObjects();
            String[] brigadesNames = brigades.stream()
                    .map(Brigade::getName)
                    .toArray(String[]::new);

            ArrayList<Work> allWorks = this.worksDataSource.getListOfSourceObjects();

            JFrame addWindow = new JFrame("Dodawanie zlecenia");
            addWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel worksLabel = new JLabel("Works: ");
            Work[] listItemAbstracts = allWorks.toArray(Work[]::new);
            JList worksList = new JList(listItemAbstracts);

            worksList.setCellRenderer(new CheckListWorkRenderer());
            worksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            worksList.addMouseListener(new WorkListMouseEvent());

            JLabel isPlannedLabel = new JLabel("Job status: ");
            JCheckBox isPlannedCheckbox = new JCheckBox();

            JLabel brigadeLabel = new JLabel("Brigade Label: ");
            JComboBox<String> brigadeCombo = new JComboBox<>(brigadesNames);

            JButton addButton = new JButton("Add");

            panel.add(brigadeLabel);
            panel.add(brigadeCombo);

            panel.add(worksLabel);
            panel.add(worksList);

            panel.add(isPlannedLabel);
            panel.add(isPlannedCheckbox);

            panel.add(addButton);

            addButton.addActionListener(e -> {

                EventQueue.invokeLater(() -> {
                    boolean isPlanned = isPlannedCheckbox.isSelected();
                    String brigadeName = (String) brigadeCombo.getSelectedItem();

                    ListModel<Work> worksModel = worksList.getModel();
                    ArrayList<Work> checkedWorks = new ArrayList<>();

                    for (int i = 0; i < worksModel.getSize(); i++) {
                        if (worksModel.getElementAt(i).isSelected()) checkedWorks.add(worksModel.getElementAt(i));
                    }

                    ArrayList<Brigade> allBrigades = this.brigadeDataSource.getListOfSourceObjects();
                    Optional<Brigade> foundBrigade = allBrigades.stream()
                            .filter(brigade -> brigade.getName().equals(brigadeName)).findFirst();

                    if (foundBrigade.isPresent()) {
                        Brigade brigade = foundBrigade.get();

                        Job newJob = Job.createJob(isPlanned, checkedWorks, brigade);

                        this.jobDataSource.saveObject(newJob);

                        ListModel currentModel = this.list.getModel();

                        DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();

                        for (int i = 0; i < currentModel.getSize(); i++) {
                            newModel.addElement((CheckListItemAbstract) currentModel.getElementAt(i));
                        }

                        newModel.addElement(newJob);

                        this.list.setModel(newModel);

                        JOptionPane.showMessageDialog(addWindow, "Dodano nowe zlecenie:  " + newJob.toString());
                        addWindow.dispose();

                    }

                });
            });

            addWindow.getContentPane().add(panel);
            addWindow.pack();
            addWindow.setLocationRelativeTo(null);
            addWindow.setVisible(true);

        });
    }

    @Override
    public void showWorksForJob() {

    }
}
