package game.scripts.weapons.cannon;

import game.levels.Level;
import game.prefabs.Bullet;
import game.scripts.animations.Animation;
import lib.Object2D;

import java.awt.*;
import java.util.List;

public class CannonFlyAnimation extends Animation {

    public List<String> excludeTags;

    public Object2D trash;

    public CannonFlyAnimation(List excludeTags, Object2D trash){
        this.excludeTags = excludeTags;
        this.trash = trash;
    }
    public Point getRotatedPosition(float offsetX, float offsetY, float rotation){
        double rad = Math.toRadians(rotation);

        float rotatedX = (float) (offsetX * Math.cos(rad) - offsetY * Math.sin(rad));
        float rotatedY = (float) (offsetX * Math.sin(rad) + offsetY * Math.cos(rad));

        return new Point(
                Math.round(object.xPos + rotatedX),
                Math.round(object.yPos + rotatedY)
        );
    }
    @Override
    public void start(){

        Point point = ((Bullet)(object)).target;
        float xDist = point.x - object.xPos;
        float yDist = point.y - object.yPos;
        float len = (float) Math.sqrt(xDist * xDist + yDist * yDist);
        //xDist /= len;
        //yDist /= len;

        addKeyframe(0, object.xPos, object.yPos, object.xSize, object.ySize, object.rotation, object.color);
        addKeyframe(
                1,
                object.xPos + xDist / 2,
                object.yPos + yDist / 2,
                object.xSize+20,
                object.ySize+20,
                object.rotation,
                object.color
        );
        addKeyframe(
                1,
                object.xPos + xDist,
                object.yPos + yDist,
                object.xSize,
                object.ySize,
                object.rotation,
                object.color
        );
        play();
    }
    @Override
    public void onAnimationEnd(){
        CannonExplosion explosion = new CannonExplosion(object.xPos, object.yPos);
        explosion.excludeTags = excludeTags;
        trash.addChild(explosion);
        object.destroy();
    }
}
