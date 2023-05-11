import java.util.HashMap;
import java.util.LinkedList;

enum WorkType {
    GENERAL,
    INSTALATION,
    DISASSEMBLY,
    REPLACEMENT
}
public class Work extends Thread {

    public static Work getObject(long id) {
        return workHashMap.get(id);
    }

    private static HashMap<Long, Work> workHashMap = new HashMap<>();

    private WorkType workType;
    private int time;
    private boolean isDone;

    private String description;

    private static long currentId = 1;
    public long id;

    public static Work createWork(WorkType workType, int time, String description) {
        Work work = new Work(workType, time, description);

        workHashMap.put(work.id, work);

        return work;
    }

    public Work(WorkType workType, int time, String description) {
        this.workType = workType;
        this.time = time;
        this.description = description;

        this.id =  currentId;
        currentId++;
    }

    public static HashMap<Long, Work> getWorkHashMap() {
        return workHashMap;
    }

    public WorkType getWorkType() {
        return workType;
    }

    public int getTime() {
        return time;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.getTime()* 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(getDescription());
        System.out.println(getWorkType());
        System.out.println(getTime());
        System.out.println(isDone());
        this.isDone = true;
        System.out.println("Current id=" + this.id);
    }

    @Override
    public String toString() {
        return "Work{" +
                "workType=" + workType +
                ", time=" + time +
                ", isDone=" + isDone +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}
