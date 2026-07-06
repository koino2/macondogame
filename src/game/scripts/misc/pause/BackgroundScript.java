package game.scripts.misc.pause;

import lib.Object2D;
import lib.Script;

import java.awt.*;
import java.util.Random;

public class BackgroundScript extends Script {

    public int paddingX = 500;
    public int paddingY = 500;

    public int target = 200;

    public float particleXVelocity = -20;
    public float particleYVelocity = 20;

    public Color particleColor = new Color(104, 41, 60);

    Random rng = new Random();

    Object2D folder;
    @Override
    public void start() {
        folder = new Object2D(0, 0, 0, 0, 0);
        object.scene.addObject(folder);
    }

    @Override
    public void update(double deltaTime) {
        int width = object.scene.engine.getWidth();
        int height = object.scene.engine.getHeight();

        target = (width*height)/10000;

        int count = folder.children.size();

        if (count < target) {
            Object2D particle = new Object2D(0, 0, 50, 50, 0);

            particle.xPos = object.scene.camera.xPos
                    + rng.nextInt(
                            -width / 2 - paddingX,
                    width / 2 + paddingX
            );
            particle.yPos = object.scene.camera.yPos
                    + rng.nextInt(
                            -height / 2 - paddingY,
                    height / 2 + paddingY
            );

            particle.xVelocity = particleXVelocity + rng.nextFloat(-10, 10);
            particle.yVelocity = particleYVelocity + rng.nextFloat(-10, 10);

            particle.zIndex = -20;

            particle.color = particleColor;

            particle.addScript(new DestroyParticleOutOfBounds());
            
            folder.addChild(particle);
        }
    }
}
