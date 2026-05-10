package scripts;

import lib.Object2D;
import lib.Script;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class CollisionScript extends Script {

    public List<Object2D> collidableObjects = new ArrayList<>();

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {

        for (int i = 0; i < collidableObjects.size(); i++) {

            Point2D.Float mtv = object.getMTV(collidableObjects.get(i));

            if (mtv != null) {

                object.xPos -= mtv.x;
                object.yPos -= mtv.y;

                float len = (float)Math.sqrt(mtv.x * mtv.x + mtv.y * mtv.y);
                if (len != 0) {
                    float nx = mtv.x / len;
                    float ny = mtv.y / len;

                    float dot = object.xVelocity * nx + object.yVelocity * ny;

                    object.xVelocity -= dot * nx;
                    object.yVelocity -= dot * ny;
                }
            }
        }

    }

    @Override
    public void renderUI(Graphics g) {

    }
}