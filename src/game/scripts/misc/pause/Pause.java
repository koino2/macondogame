package game.scripts.misc.pause;

import game.levels.SampleScene;
import lib.Input;
import lib.Scene;
import lib.Script;

import java.awt.event.KeyEvent;

public class Pause extends Script {

    Scene pauseScene;

    @Override
    public void start() {

    }

    int frame = 0;

    @Override
    public void update(double deltaTime) {
        frame++;
        if(Input.isKeyPressed(KeyEvent.VK_ESCAPE) && frame > 1){
            pauseScene = new PauseScene(object.scene);
            frame = 0;
            object.scene.engine.changeScene(pauseScene);
        }
    }
}
