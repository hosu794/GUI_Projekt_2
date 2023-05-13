import javax.swing.*;
import java.awt.*;

public class Application extends JPanel   {

    JButton button;

    LogoutListener logoutListener;

    public Application(LogoutListener logoutListener) {

        this.logoutListener = logoutListener;
        this.setBackground(Color.BLUE);

        this.button = new JButton("Start");
        this.setLayout(new BorderLayout());
        this.add( button, BorderLayout.PAGE_END);
        setSize(500, 500);
        this.setVisible(true);

        this.button.addActionListener(e -> {

            if (logoutListener != null) {
            logoutListener.onLogout(true);

            JOptionPane.showConfirmDialog(this, "Wylogowano pomyślnie!");

            }

        });


    }


}
