package nz.pumbas.FileManager;

import java.io.*;
import java.util.ArrayList;

public class FileManager
{
    public static String[] read(String fileName) {
        ArrayList<String> result = new ArrayList<>();

        try {
            //File didn't exist, so we created one - Thus it had no information
            if(new File(fileName).createNewFile()) {
                return new String[0];
            }


            BufferedReader br = new BufferedReader(new FileReader(fileName));
            br.readLine(); //Skip the header
            String line;

            while ((line = br.readLine()) != null) {
                result.add(line);
            }

            br.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return result.toArray(new String[0]);
    }

    public static boolean write(String filename, String[] text) {
        return write(filename, text, false);
    }

    public static boolean write(String filename, String[] text, boolean append)
    {
        boolean isSuccessful = false;

        try
        {
            FileWriter fileWriter = new FileWriter(filename, append);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String line : text)
            {
                bufferedWriter.newLine();
                bufferedWriter.write(line);
            }

            bufferedWriter.close();
            isSuccessful = true;

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return isSuccessful;
    }
}
