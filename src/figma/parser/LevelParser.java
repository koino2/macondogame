package figma.parser;

import game.prefabs.enemies.*;
import game.prefabs.misc.PressurePlate;
import lib.Object2D;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class LevelParser {
    public List<Object2D> objects = new ArrayList<>();
    public List<Enemy> enemies = new ArrayList<>();
    public Object2D spawnPoint;
    public Object2D pressurePlate;

    public File level;

    public LevelParser(String path, PressurePlate plate){
        level = new File(path);
        pressurePlate = plate;
    }

    public void parse(){
        objects.clear();
        enemies.clear();

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
                float xSize = Math.round(Float.parseFloat(parts[3])+2);
                float ySize = Math.round(Float.parseFloat(parts[4])+2);
                float rot = Float.parseFloat(parts[5]);
                System.out.println(parts[6]);
                Color color = Color.decode(parts[6].substring(0, 7));

                Object2D object = new Object2D(xPos, yPos, xSize, ySize, rot);
                object.color = color;

                if (type.equals("noCollision")){
                    object.tags.add("noCollision");
                }

                objects.add(object);
            }
            if (type.equals("Spawnpoint")){
                float xPos = Float.parseFloat(parts[1]);
                float yPos = Float.parseFloat(parts[2]);
                float xSize = Math.round(Float.parseFloat(parts[3]));
                float ySize = Math.round(Float.parseFloat(parts[4]));
                float rot = Float.parseFloat(parts[5]);
                Color color = Color.decode(parts[6].substring(0, 7));

                Object2D object = new Object2D(xPos, yPos, xSize, ySize, rot);
                object.color = color;
                object.tags.add("noCollision");
                spawnPoint = object;
            }
            if (type.equals("PressurePlate")){
                float xPos = Float.parseFloat(parts[1]);
                float yPos = Float.parseFloat(parts[2]);
                float xSize = Math.round(Float.parseFloat(parts[3]));
                float ySize = Math.round(Float.parseFloat(parts[4]));
                float rot = Float.parseFloat(parts[5]);
                Color color = Color.decode(parts[6].substring(0, 7));

                pressurePlate.color = color;
                pressurePlate.xPos = xPos;
                pressurePlate.yPos = yPos;
                pressurePlate.xSize = xSize;
                pressurePlate.ySize = ySize;
                pressurePlate.rotation = rot;
                pressurePlate.tags.add("noCollision");
            }
            if (Objects.equals(type, "Enemy")) {
                System.out.println(line);
                String enemyType = parts[1];
                float xPos = Float.parseFloat(parts[2]);
                float yPos = Float.parseFloat(parts[3]);
                float xSize = Float.parseFloat(parts[4]);
                float ySize = Float.parseFloat(parts[5]);
                float rot = Float.parseFloat(parts[6]);
                Color color = Color.decode(parts[7].substring(0, 7));

                Enemy enemy = new ShooterEnemy((int) xPos, (int) yPos, (int) rot);
                switch (enemyType) {
                    case "Shooter":
                        enemy = new ShooterEnemy((int) xPos, (int) yPos, (int) rot);
                        break;
                    case "Turret":
                        enemy = new Turret((int) xPos, (int) yPos, (int) rot);
                        break;
                    case "Bomb":
                        enemy = new Bomb((int) xPos, (int) yPos, (int) rot);
                        break;
                    case "Nanobot":
                        enemy = new Nanobot((int) xPos, (int) yPos, (int) rot);
                        break;
                }
                enemy.xPos = xPos;
                enemy.yPos = yPos;
                enemy.xSize = xSize;
                enemy.ySize = ySize;
                enemy.rotation = rot;
                enemy.color = color;
                enemies.add(enemy);
            }
        }
        scanner.close();
    }
}
