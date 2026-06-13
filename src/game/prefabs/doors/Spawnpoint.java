package game.prefabs.doors;

import lib.Object2D;
import lib.Script;

import java.awt.*;

public class Spawnpoint extends Object2D {
    public double spawnTime = 0.5f;

    public Spawnpoint(float x, float y) {
        super(x, y, 200, 200, 0);
        tags.add("noCollision");
        color = new Color(154, 154, 154, 255);

        addScript(new Script() {
            LevelDoor ld;
            @Override
            public void start() {
                ld = new LevelDoor(100, 0);
                ld.doorColor = new Color(0, 0, 0);
                addChild(ld);

                Color wallColor = new Color(64, 64, 64);

                Object2D wall1 = new Object2D(-100, 0, 25, 225, 0);
                wall1.setColor(wallColor);
                addChild(wall1);

                Object2D wall2 = new Object2D(0, 100, 220, 25, 0);
                wall2.setColor(wallColor);
                addChild(wall2);

                Object2D wall3 = new Object2D(0, -100, 220, 25, 0);
                wall3.setColor(wallColor);
                addChild(wall3);
            }

            double time;
            boolean opened = false;

            @Override
            public void update(double deltaTime) {
                time += deltaTime;
                if(time > spawnTime && !opened){
                    ld.open();
                    opened = true;
                }
            }
        });
    }
}
