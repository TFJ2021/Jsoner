package fuck.json;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        // Loads file. If it doesn't exist, a new one will be created. Because a ressourcePath was set
        TheJsonCreator jsonCreator = new TheJsonCreator(Path.of("configFiles/main.json"), "configs/config.json");

        // exists: reads the right value
        String botToken = jsonCreator.getString("botToken", "UNKNOWN");
        System.out.println("botToken = " + botToken);

        // doesn't exist: ??? = fallback
        String apiKey = jsonCreator.getString("apiKey", "???");
        System.out.println("apiKey = " + apiKey);

        // Set
        int counts = jsonCreator.getInteger("timesStarted") + 1;
        System.out.println("Times stared = " + counts);
        jsonCreator.set("timesStarted", counts);

        // Entities are possible too
        PersonEntity person = jsonCreator.get("person", PersonEntity.class);
        System.out.println(person.toString());
        System.out.println();

        // List
        List<String> grades = jsonCreator.getList("grades", String.class);
        System.out.println("Grades = " + grades);
        System.out.println();

        // Entity Class List
        List<ClassEntity> bestClasses = jsonCreator.getList("bestClasses", ClassEntity.class);
        for (ClassEntity bestClass : bestClasses) System.out.println(bestClass);
        System.out.println();

        // Get Keys
        List<String> all = jsonCreator.getKeys("classes.json", false);
        for (String s : all) System.out.println("false " + s);
        all = jsonCreator.getKeys("classes.json", true);
        for (String s : all) System.out.println("true " + s);

        // Creates the path if it does not exist
        if (jsonCreator.getString("uuid") == null) jsonCreator.set("uuid", UUID.randomUUID().toString());

        // Don´t forget to save :)
        jsonCreator.save();

        // This throws an exception because the file doesn't exist
        try {
            new TheJsonCreator(Path.of("configFiles/clas.json"));
        } catch (RuntimeException ignored) {
            System.out.println("[!] Json File not found");
            System.out.println();
        }

        // Lists are also supported
        TheJsonCreator classes = new TheJsonCreator(Path.of("configFiles/classes.json"));
        List<ClassEntity> list = classes.getList("", ClassEntity.class);
        System.out.println(list);
        list.getFirst().setRoom("Toilet");
        classes.set("", list);
        classes.save();

        // Create JsonCreator by a String (Helpful for API responses)
        TheJsonCreator apiResponse = new TheJsonCreator(jsonString, null);
        System.out.println("Made by " + apiResponse.getString("global_name") + " [" + apiResponse.getString("clan.tag") + "]");

        // You can obtain a Section for easier data handling at the same path.
        TheJsonCreator clanSection = apiResponse.getSection("clan");
        System.out.println("Tag: " + clanSection.getString("tag") + ", By: " + clanSection.getString("identity_guild_id"));
        System.out.println();

        // If you want to access an object array but cont want to create a custom Class,
        // you can access with a helpful methode:
        TheJsonCreator imageArray = new TheJsonCreator(arrayExample, null);
        TheJsonCreator image = imageArray.getArraySection("images", 1);
        System.out.println(image.getInteger("height") + "x" + image.getInteger("width") + ": " + image.getString("url"));
        System.out.println();

        // You can output the class/entity as JSON
        System.out.println(apiResponse.toString());
    }

    // An example of an API (Discord to be exact)
    private static final String jsonString = """
            {
              "id": "852576677573296179",
              "username": "tfj_5183",
              "banner": null,
              "accent_color": 1118740,
              "global_name": "TFJ",
              "banner_color": "#111214",
              "clan": {
                "identity_guild_id": "428823240824061954",
                "identity_enabled": true,
                "tag": "2077",
                "badge": "bdf7998de2b7f8d2153aa572b8cf2e82"
              }
            }
            """;

    // Spotify album api
    private static final String arrayExample = """
               {
                   "images": [
                     {
                       "height": 640,
                       "url": "https://i.scdn.co/image/ab67616d0000b27342a71cca827a8d0abda07e49",
                       "width": 640
                     },
                     {
                       "height": 300,
                       "url": "https://i.scdn.co/image/ab67616d00001e0242a71cca827a8d0abda07e49",
                       "width": 300
                     },
                     {
                       "height": 64,
                       "url": "https://i.scdn.co/image/ab67616d0000485142a71cca827a8d0abda07e49",
                       "width": 64
                     }
                   ]
                 }
            """;
}