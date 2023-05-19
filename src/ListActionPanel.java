import listeners.ListActionPanelListener;

import javax.swing.*;

public class ListActionPanel extends JPanel {
    
    ListActionPanelListener listActionPanelListener;
    public ListActionPanel(ListActionPanelListener listActionPanelListener) {

        this.listActionPanelListener = listActionPanelListener;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setVisible(true);

        JButton newButton = new JButton("Nowy");
        newButton.addActionListener(e -> {
            listActionPanelListener.add();
        });
        add(newButton);

        JButton editButton = new JButton("Edycja");
        add(editButton);

        JButton deleteButton = new JButton("Usuń");
        deleteButton.addActionListener(e -> {
            listActionPanelListener.delete();
        });
        add(deleteButton);

        JLabel userInfoLabel = new JLabel("Witaj");
        add(userInfoLabel);

    }
}
