package game.scripts.level;

import game.scripts.misc.HealthScript;
import lib.Input;
import lib.Script;

import java.awt.event.KeyEvent;

public class Reset extends Script {
    boolean pressed = false;

    @Override
    public void start() {

    }

    public double time = 0;
    @Override
    public void update(double deltaTime) {
        time += deltaTime;
        if(Input.isKeyDown(KeyEvent.VK_F3) && Input.isKeyDown(KeyEvent.VK_R) && !pressed && time > 1){
            try {
                object.scene.engine.changeScene(object.scene.getClass().getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            pressed = true;
        }
    }
}
