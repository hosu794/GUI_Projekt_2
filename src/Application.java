import listeners.LogoutListener;

import javax.swing.*;
import java.awt.*;


public class Application extends JPanel   {

    LogoutListener logoutListener;

    public Application(LogoutListener logoutListener) {

        this.setBackground(Color.BLUE);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        RightColumnLayout column2Panel = new RightColumnLayout();
        LeftColumnLayout leftColumnLayout = new LeftColumnLayout(logoutListener, column2Panel);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.6;
        this.add(leftColumnLayout, gbc);

        // Dodanie panelu drugiej kolumny do layoutu
        gbc.gridx = 1;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.4;
        this.add(column2Panel, gbc);

        // Dodanie przycisku na dole
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        this.setVisible(true);

    }


}
