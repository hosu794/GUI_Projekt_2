import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main extends JFrame implements LoginListener {

    LoginForm loginForm;
    Application application;

    public Main() {
        this.setSize(500, 500);
        this.setTitle("Moja aplikacja");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginForm = new LoginForm(this);
        application = new Application();
        this.getContentPane().add(loginForm);
        this.setVisible(true);

    }
    public static void main(String[] args) {

        SwingUtilities.invokeLater(Main::new);

        EmployeesDepartment departmentOne = null;
        EmployeesDepartment departmentTwo = null;
        EmployeesDepartment departmentThree = null;
        try {

            departmentOne = EmployeesDepartment.createDepartment("Department One");
            departmentTwo = EmployeesDepartment.createDepartment("Department Two");
            departmentThree = EmployeesDepartment.createDepartment("Department Three");

            System.out.println(EmployeesDepartment.departmentsNames);

        } catch (NotUniqueNameException exception) {
            exception.printStackTrace();
        }

        // Create users

        User user1 = User.createUser("Marek", "Surna", LocalDate.of(2001, 12, 2), "marek.surna", "passowrd1", departmentTwo);
        User user2 = User.createUser("Marek", "Adamarek", LocalDate.of(2001, 12, 2), "marek.adamarek", "passowrd12", departmentTwo);
        Foreman leader = Foreman.createForeman("Adam", "Walgórka", LocalDate.now(), "adam.waligorka", "adam123", departmentOne);

        User user3 = User.createUser("Marek" ,"Koniak", LocalDate.now(), "marek.koniak", "koniak123", departmentThree);
        Foreman leader2 = Foreman.createForeman("Stefan", "Pobraniak", LocalDate.of(2002, 12, 2), "stefan.pobraniak", "password123", departmentThree);

        // Create brigades

        Brigade builders = new Brigade("Builders", leader);
        Brigade builders2 = new Brigade("Builders2", leader);
        Brigade builders3 = new Brigade("Builders3", leader);
        Brigade builders4 = new Brigade("Builders4", leader);
        Brigade builders5 = new Brigade("Builders5", leader2);

        // Adding employees to builders

        builders.addWorker(user1);

        builders2.addWorker(user1);

        builders3.addWorker(user2);
        builders3.addWorker(user1);


        builders5.addWorker(user3);

        // Create works

        Work createFence = Work.createWork(WorkType.INSTALATION, 10, "Zbudowac ogrodzenia");
        Work createFoundations = Work.createWork(WorkType.INSTALATION, 10, "Zbudowac fundamenty");

        Work createLibrary = Work.createWork(WorkType.GENERAL, 4, "Zbudowac biblioteke");

        Work createFence2 = Work.createWork(WorkType.INSTALATION, 10, "Zbudowac ogrodzenia2");
        Work createFoundations2 = Work.createWork(WorkType.INSTALATION, 10, "Zbudowac fundamenty2");

        Work createCrane = Work.createWork(WorkType.INSTALATION, 5, "Zbudowac dźwig");

        Work contructionWorkOne = Work.createWork(WorkType.GENERAL, 12, "konstrukcja drogi nr 1");
        Work contructionWorkTwo= Work.createWork(WorkType.GENERAL, 2, "konstrukcja drogi nr 2");

        // Add works to arrays

        ArrayList<Work> worksForClientOne = new ArrayList<>();
        worksForClientOne.add(createFoundations);
        worksForClientOne.add(createFence);

        ArrayList<Work> worksForClientTwo = new ArrayList<>();
        worksForClientTwo.add(createFence2);
        worksForClientTwo.add(createFoundations2);

        ArrayList<Work> worksForClientThree = new ArrayList<>();
        worksForClientThree.add(createLibrary);

        ArrayList<Work> workForClientFour = new ArrayList<>();
        workForClientFour.add(createCrane);

        ArrayList<Work> worksForClientFive = new ArrayList<>();
        worksForClientFive.add(contructionWorkOne);
        worksForClientFive.add(contructionWorkTwo);

        // Create jobs

        Job buildBuildingForClientOne = Job.createJob(true, worksForClientOne , builders);
        Job buildBuildingForClientThree = Job.createJob(true, worksForClientThree, builders3);
        Job buildBuildingForClientTwo = Job.createJob(true, worksForClientTwo, builders2);
        Job buildBuildingForClientFour = Job.createJob(true, workForClientFour, builders4);
        Job contructionJobForClientFive = Job.createJob(true, worksForClientFive, builders5);

        // Start jobs

//        buildBuildingForClientOne.startJob();
//        buildBuildingForClientThree.startJob();
//        buildBuildingForClientTwo.startJob();
//        buildBuildingForClientFour.startJob();
//        contructionJobForClientFive.startJob();
//
//        System.out.println("Job has started!!!");
//
//        System.out.println(Work.getObject(1l).getWorkType());
//        System.out.println(Work.getWorkHashMap());
//        System.out.println(Job.getJobHashMap());
//
//        System.out.println(Employee.getEmployees());
//        System.out.println(leader2.getBrigadeList());
//        System.out.println(leader2.getJobs());

//        LoginForm loginForm = new LoginForm();

    }

    @Override
    public void onLogin(boolean success) {

        System.out.println(success);

        System.out.println("DUPA DUAPA DSADAS");
        if (success) {
            getContentPane().remove(loginForm);
            getContentPane().add(application);
            revalidate();
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Nieprawidłowy login lub hasło", "Błąd logowania", JOptionPane.ERROR_MESSAGE);
        }
    }
}