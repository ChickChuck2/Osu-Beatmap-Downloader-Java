package Config;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class createConfig {
    public void CreateConfig(String OsuDirectory) {
        try {
            Map<String, Object> map = new HashMap<>();

            if (OsuDirectory.contains("Songs\\")) {
                System.out.println("DO");
            } else {
                map.put("GameFolder", String.format("%s\\", OsuDirectory));
            }

            Writer writer = new FileWriter("Config.json");
            new Gson().toJson(map, writer);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
