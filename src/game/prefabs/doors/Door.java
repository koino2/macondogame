package game.prefabs.doors;

import game.scripts.animations.Animation;
import lib.Object2D;

import java.awt.*;

public class Door extends Object2D {
    float defaultX = 200;
    float defaultY = 200;

    float defaultXSize = 100;
    float defaultYSize = 100;

    float xSizeAfter = 10;
    float ySizeAfter = 10;

    float directionX = 1f;
    float directionY = 0f;

    float time = 1f;

    public Door(Color color, float defaultX, float defaultY, float defaultXSize, float defaultYSize, float xSizeAfter, float ySizeAfter, float directionX, float directionY, float time) {
        super(defaultX, defaultY, defaultXSize, defaultYSize, 0);
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.defaultXSize = defaultXSize;
        this.defaultYSize = defaultYSize;
        this.xSizeAfter = xSizeAfter;
        this.ySizeAfter = ySizeAfter;
        this.directionX = directionX;
        this.directionY = directionY;
        this.time = time;

        this.color = color;
    }

    Animation openAnimation;
    public void open(){
        openAnimation = new Animation();
        openAnimation.addKeyframe(0f, xPos, yPos, xSize, ySize, 0);
        openAnimation.addKeyframe(
                time,
                defaultX + ((defaultXSize -xSizeAfter)/2f)*directionX,
                defaultY + ((defaultYSize -ySizeAfter)/2f)*directionY,
                openAnimation.lerp(defaultXSize, xSizeAfter, Math.abs(directionX)),
                openAnimation.lerp(defaultYSize, ySizeAfter, Math.abs(directionY)),
                0
        );
        addScript(openAnimation);
        openAnimation.play();
    }
    Animation closeAnimation;
    public void close(){
        closeAnimation = new Animation();
        closeAnimation.addKeyframe(0f, xPos, yPos, xSize, ySize, 0);
        closeAnimation.addKeyframe(time, defaultX, defaultY, defaultXSize, defaultYSize, 0);
        addScript(closeAnimation);
        closeAnimation.play();
    }
}
