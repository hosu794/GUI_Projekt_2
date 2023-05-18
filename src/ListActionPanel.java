import javax.swing.*;

public class ListActionPanel extends JPanel {
    
    
    public ListActionPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setVisible(true);

        JButton newButton = new JButton("Nowy");
        add(newButton);

        JButton editButton = new JButton("Edycja");
        add(editButton);

        JButton deleteButton = new JButton("Usuń");
        add(deleteButton);

        JLabel userInfoLabel = new JLabel("Witaj");
        add(userInfoLabel);

    }
}
