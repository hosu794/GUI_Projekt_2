import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class ListItems extends JPanel {

    public ListItems() {

        DataSource userDataSource = new DataSource<User>("users");

        System.out.println(userDataSource.getListOfSourceObjects());

        JList list = new JList<>(new Vector<>());
        list.setVisibleRowCount(10);


        this.add(new JScrollPane(list));
        this.setVisible(true);
    }
}
