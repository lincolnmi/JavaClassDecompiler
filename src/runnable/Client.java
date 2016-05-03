package runnable;

import service.Decompiler;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Jason on 2016/5/3.
 */
public class Client {

    public static void main(String[] args) {
        String separator = File.separator;
        try {
            String file = "out"+separator+"production"+separator+"JavaClassDecompiler"
                    +separator+"runnable"+separator+"TestClass.class";
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(new File(file)));
            Decompiler decompiler = new Decompiler(dataInputStream);
            decompiler.analyse();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
