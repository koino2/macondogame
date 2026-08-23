package game.scripts.misc;

import lib.Script;

public class SkyScript extends Script {
    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        float xDist = object.xPos - object.scene.camera.xPos;
        float yDist = object.yPos - object.scene.camera.yPos;
        object.xPos = object.scene.camera.xPos + (xDist / 2);
        object.yPos = object.scene.camera.yPos + (yDist / 2);
    }
}
