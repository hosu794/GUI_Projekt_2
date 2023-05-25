import listeners.ListActionPanelListener;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BasicActionList extends JPanel {
    
    ListActionPanelListener listActionPanelListener;

    public BasicActionList(ListActionPanelListener listActionPanelListener) {

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
    }

//    public static void updateLoggedInUser() {
//
//        User loggedInUser = LoggedInUser.getInstance().getUser();
//        if (loggedInUser != null) {
//            userInfoLabel.setText("Zalogowany użytkownik: " + loggedInUser.getInitial());
//        } else {
//            userInfoLabel.setText("Brak zalogowanego użytkownika");
//        }
//    }
}

class DisplayCurrentUser extends JPanel {

    static private JLabel userInfoLabel;

    public DisplayCurrentUser() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setVisible(true);
        userInfoLabel = new JLabel();
        add(userInfoLabel);
    }

    public static void updateLoggedInUser() {

        User loggedInUser = LoggedInUser.getInstance().getUser();
        if (loggedInUser != null) {
            userInfoLabel.setText("Zalogowany użytkownik: " + loggedInUser.getInitial());
        } else {
            userInfoLabel.setText("Brak zalogowanego użytkownika");
        }
    }
}
