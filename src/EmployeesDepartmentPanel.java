import listeners.EmployeesDepartmentListActionPanelListener;
import listeners.ListActionPanelListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EmployeesDepartmentPanel extends JPanel implements EmployeesDepartmentListActionPanelListener {

    DataSource departmentDataSource;
    JList list;

    public EmployeesDepartmentPanel() {
        this.departmentDataSource = new DataSource<EmployeesDepartment>("departments.txt");
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

    }

    @Override
    public void update() {

    }

    @Override
    public void add() {

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


            });
        }
    }
}
