package Engine.Utils;

import Engine.PokerHandStep;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EngineUtils {

    public static final String BASE_PACKAGE= "/Resources/Images/cards/";

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return (fileName.substring(fileName.lastIndexOf(".")+1)).toLowerCase();
        else return "";
    }


    public static void saveListToFile(List<PokerHandStep> list, String path)
    {
        try {

            FileWriter writer = new FileWriter(path);
            for (PokerHandStep step: list)
            {
                writer.write("************************************************************************************************\n");
                writer.write(step.getStepAsString());
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Circularly increment i.
     */
    public static int inc(int i, int max) {
        return (i == max) ? 0 : i + 1;
    }

}
