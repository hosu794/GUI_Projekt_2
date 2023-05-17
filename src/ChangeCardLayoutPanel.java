import javax.swing.*;
import java.awt.*;

public class ChangeCardLayoutPanel extends JPanel {

    RoundedButton logoutButton;
    ChangePageLayoutListener changePageLayoutListener;

    public ChangeCardLayoutPanel(String title, String viewName, ChangePageLayoutListener changePageLayoutListener) {

        this.logoutButton = new RoundedButton(title);
        this.changePageLayoutListener = changePageLayoutListener;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.logoutButton.addActionListener(e -> {
            this.changePageLayoutListener.onChangeLayout(viewName);
        });

        this.add(this.logoutButton);

    }
}
