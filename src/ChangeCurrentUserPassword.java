import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ChangeCurrentUserPassword extends JPanel {

    DataSource<User> userDataSource;
    DataSource<Foreman> foremanDataSource;

    RoundedButton changePasswordButton;

    public ChangeCurrentUserPassword() {

        this.userDataSource = new DataSource<>("users.txt");
        this.foremanDataSource = new DataSource<>("foremans.txt");

        this.changePasswordButton = new RoundedButton("Zmień hasło");

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(this.changePasswordButton);
        setVisible(true);

        this.changePasswordButton.addActionListener(e -> {

            EventQueue.invokeLater(() -> {
                JFrame changePasswordWindow = new JFrame("Zmiana hasła");
                changePasswordWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                changePasswordWindow.add(panel);
                changePasswordWindow.setLayout(new FlowLayout());
                changePasswordWindow.setVisible(true);

                // Initialize fields
                JLabel oldPasswordLabel = new JLabel("Old password");
                JPasswordField oldPasswordField = new JPasswordField(20);
                JLabel newPasswordLabel = new JLabel("New passwoed");
                JPasswordField newPasswordField = new JPasswordField(20);

                JButton changePasswordButton = new JButton("Zmień hasło");

                panel.add(oldPasswordLabel);
                panel.add(oldPasswordField);
                panel.add(newPasswordLabel);
                panel.add(newPasswordField);

                panel.add(changePasswordButton);

                changePasswordButton.addActionListener(e1 -> {

                    String oldPassword = new String(oldPasswordField.getPassword());
                    String newPassword = new String(newPasswordField.getPassword());

                    User user = LoggedInUser.getInstance().getUser();

                    if (!oldPassword.equals(newPassword) && oldPassword.equals(user.getPassword())) {

                        if (user instanceof Foreman foreman) {
                            // Update from foreman database
                            foreman.setPassword(newPassword);

                           ArrayList<Foreman> foremanArrayList = this.foremanDataSource.getListOfSourceObjects();

                           ArrayList<Foreman> updatedForemen = (ArrayList<Foreman>) foremanArrayList.stream()
                                   .map(foreman1 -> {
                                       if (foreman1.getLogin().equals(foreman.getLogin()))
                                            foreman1.setPassword(newPassword);
                                       return foreman1;
                                   }).collect(Collectors.toList());

                            System.out.println(updatedForemen);
                           this.foremanDataSource.updateListOfUpdate(updatedForemen);

                           LoggedInUser.getInstance().setUser(foreman);
                           ListActionPanel.updateLoggedInUser();

                           JOptionPane.showMessageDialog(this, "Zmieniono hasło dla Brygadzisty", "Powiadomienie", JOptionPane.INFORMATION_MESSAGE);
                           changePasswordWindow.dispose();

                        } else {
                            // Update from user database
                            // Update from foreman database
                            user.setPassword(newPassword);

                            ArrayList<User> allUsers = this.userDataSource.getListOfSourceObjects();

                            ArrayList<User> updatedUsers = (ArrayList<User>) allUsers.stream()
                                    .map(u -> {
                                        if (u.getLogin().equals(user.getLogin()))
                                            u.setPassword(newPassword);
                                        return u;
                                    }).collect(Collectors.toList());

                            System.out.println(updatedUsers);
                            this.userDataSource.updateListOfUpdate(updatedUsers);

                            LoggedInUser.getInstance().setUser(user);
                            ListActionPanel.updateLoggedInUser();

                            JOptionPane.showMessageDialog(this, "Zmieniono hasło dla Uzytkownika", "Powiadomienie", JOptionPane.INFORMATION_MESSAGE);
                            changePasswordWindow.dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Hasła są indentyczne lub nieprawidłowe");
                    }

                });

                changePasswordWindow.getContentPane().add(panel);
                changePasswordWindow.pack();
                changePasswordWindow.setLocationRelativeTo(null);
                changePasswordWindow.setVisible(true);

            });

        });

    }

}
