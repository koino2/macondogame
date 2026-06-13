package game.prefabs.doors;

import lib.Input;
import lib.Object2D;
import lib.Script;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LevelDoor extends Object2D {

    public Door door1;
    public Door door2;

    public Color doorColor = new Color(0, 0, 0);

    Script doorScript = new Script() {
        @Override
        public void start() {
            door1 = new Door(
                    doorColor,
                    0,
                    -50,
                    20f,
                    100f,
                    10,
                    10,
                    0f,
                    -1f,
                    1f
            );
            door2 = new Door(
                    doorColor,
                    0,
                    50,
                    20f,
                    100f,
                    10,
                    10,
                    0f,
                    1f,
                    1f
            );

            addChild(door1);
            addChild(door2);
        }

        boolean firstUpdate = false;
        @Override
        public void update(double deltaTime) {
            /*if(Input.isKeyDown(KeyEvent.VK_E)){
                open();
            }
            if(Input.isKeyDown(KeyEvent.VK_Q)){
                close();
            }*/
            if(!firstUpdate) {
                //open();
                firstUpdate = true;
            }
        }
    };

    public LevelDoor(float x, float y) {
        super(x, y, 0, 0, 0);
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
