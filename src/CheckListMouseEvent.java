import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckListMouseEvent extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent event) {
        JList list = (JList) event.getSource();
        int index = list.locationToIndex(event.getPoint());
        CheckListItemAbstract item = (CheckListItemAbstract) list.getModel()
                .getElementAt(index);
        item.setSelected(!item.isSelected());
        list.repaint(list.getCellBounds(index, index));
    }
}
