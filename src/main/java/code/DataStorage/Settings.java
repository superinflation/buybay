package code.DataStorage;

import java.io.File;
import java.io.IOException;

public class Settings {

    public static String defaultPFPPath = "src/main/resources/defaultPFP.png";
    public static String noImagePath = "src/main/resources/default.png";
    private static File file;

    public static void loadSettings() throws IOException {
        file = new File("../Settings");
        file.createNewFile();

        // ArrayList<String> settings = new Scanner(new
        // IOStream(file)).useDelimiter("\\Z").next().split("\\*");
    }

}
