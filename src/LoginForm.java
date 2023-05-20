import listeners.LoginListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;

public class LoginForm extends JPanel implements ActionListener {

    private LoginListener loginListener;

    JButton b1;
    JPanel newPanel;
    JPanel helloPanel;
    JLabel userLabel, passLabel;
    final JTextField  textField1, textField2;

    private DataSource userDataSource;
    private DataSource foremanDataSource;

    public LoginForm(LoginListener loginListener, DataSource userDataSource, DataSource foremansDataSource) {
         this.loginListener = loginListener;
         this.userDataSource = userDataSource;
         this.foremanDataSource = foremansDataSource;

        //create label for username
        userLabel = new JLabel();
        userLabel.setText("Username");      //set label value for textField1

        //create text field to get username from the user
        textField1 = new JTextField(15);    //set length of the text

        //create label for password
        passLabel = new JLabel();
        passLabel.setText("Password");      //set label value for textField2

        //create text field to get password from the user
        textField2 = new JPasswordField(15);    //set length for the password

        //create submit button
        b1 = new JButton("SUBMIT"); //set label to button

        //create panel to put form elements
        newPanel = new JPanel(new GridLayout(3, 1));
        newPanel.add(userLabel);    //set username label to panel
        newPanel.add(textField1);   //set text field to panel
        newPanel.add(passLabel);    //set password label to panel
        newPanel.add(textField2);   //set text field to panel
        newPanel.add(b1);           //set button to panel


        helloPanel = new JPanel(new GridLayout(1,1));


        //set border to panel
        add(newPanel, BorderLayout.CENTER);

        //perform action on button click
        b1.addActionListener(this);     //add action listener to button
//        setTitle("LOGIN FORM");
        setSize(300, 200);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String userValue = textField1.getText();
        String passValue = textField2.getText();

        System.out.println(userValue);
        System.out.println(passValue);

        ArrayList<User> userArrayList = userDataSource.getListOfSourceObjects();
        ArrayList<Foreman> foremanArrayList = foremanDataSource.getListOfSourceObjects();

        System.out.println(foremanArrayList);

        Optional<User> userResult = userArrayList.stream()
                .filter(user -> user.getLogin().equals(userValue) && user.getPassword().equals(passValue))
                .findFirst();

        Optional<Foreman> foremanResult = foremanArrayList.stream()
                .filter(foreman -> foreman.getLogin().equals(userValue) && foreman.getPassword().equals(passValue))
                .findFirst();


         if (userResult.isPresent()) {
            User user = userResult.get();

            if (loginListener != null) {
                loginListener.onLogin(true);
            }

            LoggedInUser.getInstance().setUser(user);

            ListActionPanel.updateLoggedInUser();

            JOptionPane.showMessageDialog(this, "Zalogowano pomyślnieś User'a");

            System.out.println(user);
        } else {

            if(foremanResult.isPresent()) {

                Foreman foreman = foremanResult.get();

                if (loginListener != null) {
                    loginListener.onLogin(true);
                }

                LoggedInUser.getInstance().setUser(foreman);
                ListActionPanel.updateLoggedInUser();

                JOptionPane.showMessageDialog(this, "Zalogowano pomyślnieś Foreman'a");

            } else {
                System.out.println("User not found!");

                JOptionPane.showMessageDialog(this, "Nieudane logowanie. Sprawdź swoje dane.", "Błąd logowania", JOptionPane.ERROR_MESSAGE);
            }

        }


    }


}
