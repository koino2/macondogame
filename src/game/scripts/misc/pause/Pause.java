package game.scripts.misc.pause;

import game.scripts.misc.Settings;
import lib.Input;
import lib.Script;

import java.awt.event.KeyEvent;

public class Pause extends Script {

    PauseScene pauseScene;

    public Settings settings;

    public Pause(){

    }

    @Override
    public void start() {
        this.settings = new Settings();
        object.addScript(settings);
    }

    int frame = 0;

    @Override
    public void update(double deltaTime) {
        frame++;
        if(Input.isKeyPressed(KeyEvent.VK_ESCAPE) && frame > 1){
            pauseScene = new PauseScene(object.scene);
            pauseScene.sceneSettings = settings;
            frame = 0;
            object.scene.engine.changeScene(pauseScene);
        }
    }
}
