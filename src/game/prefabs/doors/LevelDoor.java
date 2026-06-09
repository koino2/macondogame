package game.prefabs.doors;

import lib.Input;
import lib.Object2D;
import lib.Script;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LevelDoor extends Object2D {

    public Door door1;
    public Door door2;

    Script doorScript = new Script() {
        @Override
        public void start() {
            door1 = new Door(
                    new Color(64, 109, 255),
                    -50,
                    yPos,
                    100f,
                    100f,
                    10,
                    10,
                    -1f,
                    0f,
                    1f
            );
            door2 = new Door(
                    new Color(255, 92, 92),
                    50,
                    yPos,
                    100f,
                    100f,
                    10,
                    10,
                    1f,
                    0f,
                    1f
            );

            addChild(door1);
            addChild(door2);
        }

        @Override
        public void update(double deltaTime) {
            if(Input.isKeyDown(KeyEvent.VK_E)){
                open();
            }
            if(Input.isKeyDown(KeyEvent.VK_Q)){
                close();
            }
        }
    };

    public LevelDoor() {
        super(0, 0, 0, 0, 0);

        addScript(doorScript);
    }

    public void open(){
        door1.open();
        door2.open();
    }

    public void close(){
        door1.close();
        door2.close();
    }
}
