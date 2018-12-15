package gr.teicm.ieee.madc.disasternotifierandroid.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class FileService {
    public static boolean existsFile(String inFIS) {
        return new File(inFIS).exists();
    }

    public void saveFile(String inFOS, Object object) throws IOException {

        FileOutputStream fos = new FileOutputStream(inFOS);
        ObjectOutputStream oos;

        oos = new ObjectOutputStream(fos);
        oos.writeObject(object);

        oos.close();
        fos.close();
    }

    public Object readFile(String inFIS) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(inFIS);
        ObjectInputStream is = new ObjectInputStream(fis);

        Object object = is.readObject();

        is.close();
        fis.close();

        return object;
    }

    public boolean deleteFile(String inFIS) {
        return new File(inFIS).delete();
    }
}
