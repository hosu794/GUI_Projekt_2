import listeners.ChangePageLayoutListener;

import javax.swing.*;
import java.awt.*;

public class RightColumnLayout extends JPanel implements ChangePageLayoutListener {

    CardLayout cardLayout;

    public RightColumnLayout() {
        this.setBackground(Color.GREEN); // Ustawienie koloru dla drugiej kolumny

        this.cardLayout = new CardLayout();

        this.setLayout(this.cardLayout);

        // Komponenty w drugiej kolumnie
        this.add(new EmployeesDepartmentPanel(), "Panel 1");
        this.add(new EmployeePanel(), "Panel 2");
        this.add(new UsersPanel(), "Panel 3");
        this.add(new ForemanPanel(), "Panel 4");
        this.add(new BrigadesPanel(), "Panel 5");
        this.add(new JLabel("Panel 6"), "Panel 6");
        this.add(new WorksPanel(), "Panel 7");
    }

    @Override
    public void onChangeLayout(String layoutName) {
        this.cardLayout.show(this, layoutName);
    }
}
