import javax.swing.*;
import java.awt.*;

public class RightColumnLayout extends JPanel implements ChangePageLayoutListener {

    CardLayout cardLayout;

    public RightColumnLayout() {
        this.setBackground(Color.GREEN); // Ustawienie koloru dla drugiej kolumny

        this.cardLayout = new CardLayout();

        this.setLayout(this.cardLayout);

        // Komponenty w drugiej kolumnie
        this.add(new JLabel("Panel 1"), "Panel 1");
        this.add(new JLabel("Panel 2"), "Panel 2");
        this.add(new JLabel("Panel 3"), "Panel 3");
        this.add(new JLabel("Panel 4"), "Panel 4");
        this.add(new JLabel("Panel 5"), "Panel 5");
        this.add(new JLabel("Panel 6"), "Panel 6");
        this.add(new JLabel("Panel 7"), "Panel 7");
    }

    @Override
    public void onChangeLayout(String layoutName) {
        this.cardLayout.show(this, layoutName);
    }
}
