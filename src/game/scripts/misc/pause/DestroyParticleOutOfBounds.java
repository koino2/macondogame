package game.scripts.misc.pause;

import lib.Script;

public class DestroyParticleOutOfBounds extends Script {

    public int padding = 800;

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        int width = object.scene.engine.getWidth();
        int height = object.scene.engine.getHeight();

        int screenX = (int) (object.xPos - object.scene.camera.xPos);
        int screenY = (int) (object.yPos - object.scene.camera.yPos);

        if (
            screenX < -width/2 - padding ||
            screenY > width + padding ||
            screenY < -height/2 - padding ||
            screenY > height + padding
        ) {
            object.destroy();
        }
    }
}
