package ex4_java_client;

import java.io.FileWriter;


//class which temporary saves graph for later loads.
public class fileWriter {

    public void write(String input, String address){
        try {

            // attach a file to FileWriter
            FileWriter fw
                    = new FileWriter(address);

            // read each character from string and write
            // into FileWriter
            for (int i = 0; i < input.length(); i++)
                fw.write(input.charAt(i));


            // close the file
            fw.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }
}
