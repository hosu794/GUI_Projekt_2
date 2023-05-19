import listeners.LogoutListener;

import javax.swing.*;

public class LogoutPanel extends JPanel {

    LogoutListener logoutListener;
    RoundedButton logoutButton;

    public LogoutPanel(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;

        this.logoutButton = new RoundedButton("Wyloguj się");

        this.logoutButton.addActionListener(e -> {

            if (logoutListener != null) {
                logoutListener.onLogout(true);
                JOptionPane.showMessageDialog(this, "Wylogowano pomyślnie!");
            }

        });

        this.add(this.logoutButton);

    }





}

