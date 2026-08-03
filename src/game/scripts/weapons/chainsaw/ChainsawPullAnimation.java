package game.scripts.weapons.chainsaw;

import game.scripts.animations.Animation;
import lib.Object2D;

import java.awt.*;
import java.awt.geom.Point2D;

public class ChainsawPullAnimation extends Animation {
    public Point getRotatedPosition(float offsetX, float offsetY, float rotation){
        double rad = Math.toRadians(rotation);

        float rotatedX = (float) (offsetX * Math.cos(rad) - offsetY * Math.sin(rad));
        float rotatedY = (float) (offsetX * Math.sin(rad) + offsetY * Math.cos(rad));

        return new Point(
                Math.round(object.xPos + rotatedX),
                Math.round(object.yPos + rotatedY)
        );
    }
    public Object2D pullTowards;
    public ChainsawPullAnimation(Object2D pullTowards){
        this.pullTowards = pullTowards;
    }

    @Override
    public void start() {
        addKeyframe(0, object.xPos, object.yPos, object.xSize, object.ySize, object.rotation, object.color);

        float distX = pullTowards.globalX - object.globalX;
        float distY = pullTowards.globalY - object.globalY;
        //float dist = (float) Math.sqrt(distX * distX + distY * distY);

        //if (dist < 50) return;

        Point2D.Float pulledPoint = new Point2D.Float(
                object.xPos + distX / 2,
                object.yPos + distY / 2
        );

        addKeyframe(
                1,
                pulledPoint.x,
                pulledPoint.y,
                object.xSize,
                object.ySize,
                object.rotation,
                object.color
        );

        play();
    }
}
