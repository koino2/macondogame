package figma.parser;

import lib.Object2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class LevelParser {
    public List<Object2D> objects = new ArrayList<>();
    public List<Object2D> enemies = new ArrayList<>();

    public File level;

    public LevelParser(String path){
        level = new File(path);
    }

    public void parse(){
        Scanner scanner;
        try {
            scanner = new Scanner(level);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] parts = line.split(" ");

            String type = parts[0];

            if (type.equals("Rectangle") || type.equals("Wall") || type.equals("noCollision")){
                float xPos = Float.parseFloat(parts[1]);
                float yPos = Float.parseFloat(parts[2]);
                float xSize = Float.parseFloat(parts[3]);
                float ySize = Float.parseFloat(parts[4]);
                float rot = Float.parseFloat(parts[5]);

                Object2D object = new Object2D(xPos, yPos, xSize, ySize, rot);

                if (type.equals("noCollision")){
                    object.tags.add("noCollision");
                }

                objects.add(object);
            } else if (Objects.equals(type, "enemy")) {
                // do enemy stuff
            }
        }
        scanner.close();
    }
}
