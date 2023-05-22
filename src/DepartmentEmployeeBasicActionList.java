import listeners.EmployeesDepartmentListActionPanelListener;

import javax.swing.*;

public class DepartmentEmployeeBasicActionList extends JPanel {

    EmployeesDepartmentListActionPanelListener listActionPanelListener;

    public DepartmentEmployeeBasicActionList(EmployeesDepartmentListActionPanelListener listActionPanelListener) {
        this.listActionPanelListener = listActionPanelListener;
    }

}
