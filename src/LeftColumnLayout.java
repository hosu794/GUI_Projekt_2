import listeners.ChangePageLayoutListener;
import listeners.LogoutListener;

import javax.swing.*;
import java.awt.*;

public class LeftColumnLayout extends JPanel {

        LogoutListener logoutListener;

       public LeftColumnLayout(LogoutListener logoutListener, ChangePageLayoutListener changePageLayoutListener) {

           this.setBackground(Color.YELLOW);
           this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


           ChangeCardLayoutPanel staffDepartmentChangeLayoutPanel = new ChangeCardLayoutPanel("Dział pracowników", "Panel 1", changePageLayoutListener);
           this.add(staffDepartmentChangeLayoutPanel);

           ChangeCardLayoutPanel workerChangeLayoutPanel = new ChangeCardLayoutPanel("Pracownik", "Panel 2", changePageLayoutListener);
           this.add(workerChangeLayoutPanel);

           ChangeCardLayoutPanel userChangeLayoutPanel = new ChangeCardLayoutPanel("Uzytkownik", "Panel 3", changePageLayoutListener);
           this.add(userChangeLayoutPanel);

           ChangeCardLayoutPanel foremanChangeLayoutPanel = new ChangeCardLayoutPanel("Brygadzista", "Panel 4", changePageLayoutListener);
           this.add(foremanChangeLayoutPanel);

           ChangeCardLayoutPanel brigadeChangeLayoutPanel = new ChangeCardLayoutPanel("Brygada", "Panel 5", changePageLayoutListener);
           this.add(brigadeChangeLayoutPanel);

           ChangeCardLayoutPanel jobChangeLayoutPanel = new ChangeCardLayoutPanel("Zlecenie", "Panel 6", changePageLayoutListener);
           this.add(jobChangeLayoutPanel);

           ChangeCardLayoutPanel workChangeLayoutPanel = new ChangeCardLayoutPanel("Praca", "Panel 7", changePageLayoutListener);
           this.add(workChangeLayoutPanel);

           ChangeCurrentUserPassword changeCurrentUserPassword = new ChangeCurrentUserPassword();
           this.add(changeCurrentUserPassword);

           LogoutPanel logoutPanel = new LogoutPanel(logoutListener);
           this.add(logoutPanel);





       }


}
