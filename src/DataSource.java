import java.io.*;
import java.util.ArrayList;

public class DataSource<T extends Serializable> {

    private static final long serialVersionUID = 4L;

    private String sourceFilename;

    public DataSource(String sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    private void initArrayList() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(this.sourceFilename);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            ArrayList<T> sourceObjects = new ArrayList<>();

            objectOutputStream.writeObject(sourceObjects);

            fileOutputStream.close();
            objectOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<T> getListOfSourceObjects() {

        ArrayList<T> sourceObjects = null;

        try {

            File f = new File(this.sourceFilename);

            if(f.exists()) {

                FileInputStream fileInputStream = new FileInputStream(this.sourceFilename);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                sourceObjects = (ArrayList<T>) objectInputStream.readObject();

                fileInputStream.close();
                objectInputStream.close();

            } else {
                sourceObjects = new ArrayList<>();
            }

        System.out.println(sourceObjects);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return sourceObjects;

    }

    public void saveObject(T sourceObject)  {
        try {

        ArrayList<T> sourceObjects;

        File f = new File(this.sourceFilename);
        if(f.exists()) {
            sourceObjects = (ArrayList<T>) this.getListOfSourceObjects();
        } else {
            sourceObjects = new ArrayList<>();
        }

        FileOutputStream fileOutputStream = null;


        fileOutputStream = new FileOutputStream(this.sourceFilename);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        sourceObjects.add(sourceObject);

        objectOutputStream.writeObject(sourceObjects);

        fileOutputStream.close();
        objectOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
