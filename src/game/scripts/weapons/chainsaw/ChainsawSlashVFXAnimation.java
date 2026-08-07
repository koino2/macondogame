package game.scripts.weapons.chainsaw;

import game.scripts.animations.Animation;

import java.awt.*;

public class ChainsawSlashVFXAnimation extends Animation {
    public int initialSize = 100;
    public int finalSize = 200;
    @Override
    public void start() {
        addKeyframe(0, object.xPos, object.yPos, initialSize, initialSize, object.rotation, object.color);
        addKeyframe(1f, object.xPos, object.yPos, finalSize, finalSize, object.rotation,
                new Color(object.color.getRed(), object.color.getGreen(), object.color.getBlue(), 0));
        play();;
    }

    @Override
    public void onAnimationEnd() {
        object.destroy();
    }
}
