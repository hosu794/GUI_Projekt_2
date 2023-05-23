import listeners.EmployeesDepartmentListActionPanelListener;

import javax.swing.*;

public class DepartmentEmployeeBasicActionList extends JPanel {

    EmployeesDepartmentListActionPanelListener listActionPanelListener;

    public DepartmentEmployeeBasicActionList(EmployeesDepartmentListActionPanelListener listActionPanelListener) {
        this.listActionPanelListener = listActionPanelListener;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setVisible(true);

        JButton newButton = new JButton("Nowy");
        newButton.addActionListener(e -> {
            listActionPanelListener.add();
        });
        add(newButton);

        JButton editButton = new JButton("Edycja");
        editButton.addActionListener(e -> {
            listActionPanelListener.update();
        });
        add(editButton);

        JButton deleteButton = new JButton("Usuń");
        deleteButton.addActionListener(e -> {
            listActionPanelListener.delete();
        });
        add(deleteButton);

        JButton getEmployeesForDepartmentBytton = new JButton("Sprawdz pracowników");
        getEmployeesForDepartmentBytton.addActionListener(e -> {
            listActionPanelListener.departmentEmployees();
        });
        add(getEmployeesForDepartmentBytton);

        DisplayCurrentUser displayCurrentUser = new DisplayCurrentUser();
        add(displayCurrentUser);

    }

}
