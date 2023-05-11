import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

enum JobPlan {
    PLANNED,
    NOT_PLANNED
}

enum JobStatus {
    PLANNED,
    NOT_PLANNED,
    PENDING,
    COMPLETED
}

public class Job implements Runnable{

    public static Job getObject(long id) {
        return jobHashMap.get(id);
    }

    private static long currentId = 1;
    public long id;
    public static ArrayList<Job> jobs = new ArrayList<>();
    private static HashMap<Long, Job> jobHashMap = new HashMap<>();
    private ArrayList<Work> works;
    private Brigade brigade;
    private LocalDateTime creationDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private JobStatus status;

    public Job(boolean isPlanned) {
        this.setIsPlanned(isPlanned);
    }

    public static Job createJob(boolean isPlanned) {
        Job job = new Job(isPlanned);
        jobHashMap.put(job.id, job);

        return job;
    }

    public Job(ArrayList<Work> workList, Brigade brigade) {
        this.brigade = brigade;
        this.works = workList;
        this.creationDate = LocalDateTime.now();
        this.id = currentId;
        currentId++;
    }

    public static Job createJob(ArrayList<Work> workList, Brigade brigade) {
        Job job = new Job(workList, brigade);

        jobHashMap.put(job.id, job);

        return job;
    }

    public Job(boolean isPlanned, Brigade brigade) {
        this(new ArrayList<>(), brigade);
        setIsPlanned(isPlanned);
    }

    public static Job createJob(boolean isPlanned, Brigade brigade) {
        Job job = new Job(isPlanned, brigade);

        jobHashMap.put(job.id, job);

        return job;
    }

    public Job(boolean isPlanned, ArrayList<Work> workList) {
        this(workList, null);
        setIsPlanned(isPlanned);
    }

    public static Job createJob(boolean isPlanned, ArrayList<Work> workList) {
        Job job = new Job(isPlanned, workList);

        jobHashMap.put(job.id, job);

        return job;
    }

    public Job(boolean isPlanned, ArrayList<Work> workList, Brigade brigade) {
        this(workList, brigade);
        setIsPlanned(isPlanned);
    }

    public static Job createJob(boolean isPlanned, ArrayList<Work> workList, Brigade brigade) {
        Job job = new Job(isPlanned, workList, brigade);

        jobHashMap.put(job.id, job);
        return job;
    }

    public static HashMap<Long, Job> getJobHashMap() {
        return jobHashMap;
    }

    private void setIsPlanned(boolean isPlanned) {
        if (isPlanned) this.status = JobStatus.PLANNED;
        else this.status = JobStatus.NOT_PLANNED;
    };

    public boolean setBrigade(Brigade brigade) {
        if(this.brigade != null) return false;

        this.brigade = brigade;

        return true;
    }

    public void addWork(Work work) {
        this.works.add(work);
    }

    public void startJob() {
        this.run();
    }

    @Override
    public void run() {

        ArrayList<Employee> usedEmployees = this.brigade.getEmployeeList();

        if(this.works.size() > 0 || this.brigade != null) {

            this.brigade.getForeman().getBrigades().get(this.brigade).add(this);

            this.startDate = LocalDateTime.now();
            this.status = JobStatus.PENDING;

            this.brigade.getForeman().getJobs().add(this);
            this.brigade.getForeman().getBrigadeList().add(this.brigade);

            System.out.println("Used employees: " + usedEmployees + " for id=" + this.id);

            Thread thread = new Thread(() -> {

                if(!isAreAllWorkersFree(usedEmployees)) {
                    System.out.println("Employees from brigade are already in another for job id="+ this.id);
                    while(!isAreAllWorkersFree(usedEmployees)) {
                        System.out.println("Wait for employees for job for id=" + this.id);
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("Employees are free for id=" + this.id);
                }

                jobs.add(this);

                for(int i = this.works.size() - 1 ; i >= 0; i--) {
                    Work work = this.works.get(i);
                    work.start();
                    try {
                        work.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.println("Wszytkie prace dla job_id="+ this.id + " wykonane !");
                this.endDate = LocalDateTime.now();
                this.status = JobStatus.COMPLETED;

            });

            thread.start();

        } else {
            System.out.println("Works're empty or brigate is not defined!");
        }

    }

    // method for non-stream way
    private synchronized static boolean areAllWorkersFreeIterations(ArrayList<Employee> usedEmployees) {
        boolean areAllWorkersFree = true;

        for (Job job: jobs) {

            int amountOfUsedEmployees = 0;
            int amountOfNotDoneJobs = 0;

            for (Employee employee: job.brigade.getEmployeeList()) {
                if (usedEmployees.contains(employee)) amountOfUsedEmployees++;
            }

            for (Work work : job.works) {
                if (!work.isDone()) amountOfNotDoneJobs++;
            }

            if (amountOfNotDoneJobs > 0 && amountOfUsedEmployees > 0) areAllWorkersFree = false;

        }

        return areAllWorkersFree;
    }

    private synchronized static boolean isAreAllWorkersFree(ArrayList<Employee> usedEmployees) {
        int amount = (int) jobs.stream()
                .filter(job -> job.brigade.getEmployeeList()
                        .stream()
                        .anyMatch(usedEmployees::contains))
                .filter(job -> job.works
                        .stream()
                        .anyMatch(work -> !work.isDone()))
                .count();

        return (amount == 0);
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", works=" + works +
                ", brigade=" + brigade +
                ", creationDate=" + creationDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }

    public JobStatus getStatus() {
        return status;
    }
}
