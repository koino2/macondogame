package lib.particles;

import lib.Script;

import java.awt.*;

public class ParticleScript extends Script {

    public float lifetime = 1;

    @Override
    public void start() {
        object.tags.add("particle");
        object.tags.add("noCollision");
    }

    double time = 0;

    @Override
    public void update(double deltaTime) {
        time += deltaTime;
        
        if (time > lifetime){
            object.destroy();
        }

        int alpha = (int)(255 * (1 - time / lifetime));
        alpha = Math.max(0, Math.min(255, alpha));

        object.color = new Color(
                object.color.getRed(),
                object.color.getGreen(),
                object.color.getBlue(),
                alpha
        );
    }
}
