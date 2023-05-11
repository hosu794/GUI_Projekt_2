import javax.swing.*;
import java.awt.*;

public class Application extends JPanel {

    JButton button;

    public Application() {
        this.setBackground(Color.BLUE);

        this.button = new JButton("Start");
        this.setLayout(new BorderLayout());
        this.add( button, BorderLayout.PAGE_END);
        this.setVisible(true);
    }

}
