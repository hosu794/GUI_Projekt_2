import listeners.ListActionPanelListener;

import javax.swing.*;

public class ListActionPanel extends JPanel {
    
    ListActionPanelListener listActionPanelListener;
    static private JLabel userInfoLabel;

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
        editButton.addActionListener(e -> {
            listActionPanelListener.update();
        });
        add(editButton);

        JButton deleteButton = new JButton("Usuń");
        deleteButton.addActionListener(e -> {
            listActionPanelListener.delete();
        });
        add(deleteButton);

        userInfoLabel = new JLabel("Witaj");
        add(userInfoLabel);

    }

    public static void updateLoggedInUser() {

        User loggedInUser = LoggedInUser.getInstance().getUser();
        if (loggedInUser != null) {
            userInfoLabel.setText("Zalogowany użytkownik: " + loggedInUser.getLogin());
        } else {
            userInfoLabel.setText("Brak zalogowanego użytkownika");
        }
    }
}
