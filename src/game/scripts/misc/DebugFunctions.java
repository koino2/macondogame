package game.scripts.misc;

import lib.Input;
import lib.Script;

import java.awt.event.KeyEvent;

public class DebugFunctions extends Script {
    @Override
    public void start() {

    }

    boolean pressed = false;
    @Override
    public void update(double deltaTime) {
        if(Input.isKeyDown(KeyEvent.VK_F3) && Input.isKeyDown(KeyEvent.VK_P)){
            if(!pressed) {
                pressed = true;
                object.scene.postProcessingEnabled = !object.scene.postProcessingEnabled;
            }
        } else {
            pressed = false;
        }
    }
}
