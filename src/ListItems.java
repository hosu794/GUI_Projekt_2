import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;


public class ListItems extends JPanel implements ListActionPanelListener {

    private JList<User> list;

    public ListItems() {

        DataSource userDataSource = new DataSource<User>("users.txt");


        ArrayList<User> users = userDataSource.getListOfSourceObjects();

        /// Map users to another type
        int usersCount = users.size();

        CheckListItemAbstract[] listItemAbstracts = new CheckListItemAbstract[usersCount];

        for(int i = 0; i < usersCount; i++) {
            listItemAbstracts[i] = users.get(i);
        }

        JList list = new JList(listItemAbstracts);

        list.setCellRenderer(new CheckListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint());// Get index of item
                // clicked
                CheckListItemAbstract item = (CheckListItemAbstract) list.getModel()
                        .getElementAt(index);
                item.setSelected(!item.isSelected()); // Toggle selected state
                list.repaint(list.getCellBounds(index, index));// Repaint cell
            }
        });
        setVisible(true);

        JButton button = new JButton("Usuń");
        button.addActionListener(e -> {
            ListModel<CheckListItemAbstract> model = list.getModel();
            int size = model.getSize();
            System.out.println(size);
            ArrayList<CheckListItemAbstract> remainingItems = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                CheckListItemAbstract item = model.getElementAt(i);
                if (!item.isSelected()) {
                    remainingItems.add(item);
                }
            }
            System.out.println(remainingItems);

            DefaultListModel<CheckListItemAbstract> newModel = new DefaultListModel<>();
            for (CheckListItemAbstract item : remainingItems) {
                newModel.addElement(item);
            }
            list.setModel(newModel);
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new ListActionPanel());
        add(new JScrollPane(list));
        add(button);
    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public void add() {

    }
}

class CheckListRenderer extends JCheckBox implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((CheckListItemAbstract) value).isSelected());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setText(value.toString());
        return this;
    }
}

