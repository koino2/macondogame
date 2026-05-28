package lib;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public abstract class CollisionScript extends Script {

    public List<Object2D> collidableObjects = new ArrayList<>();

    public List<String> collidableTags = new ArrayList<>();

    public abstract void onCollide(Object2D other);

    public void resolveCollision(Object2D other){
        Point2D.Float mtv = object.getMTV(other);

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

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {

        for (int i = 0; i < collidableObjects.size(); i++) {

            /*if(collidableObjects.get(i) == object){continue;}
            boolean canCollide = false;
            for (int j = 0; j < collidableTags.size(); j++) {
                if(!collidableObjects.get(i).tags.contains(collidableTags.get(j))){
                    //canCollide = false;
                } else{
                    canCollide = true;
                }
            }
            if(!canCollide){continue;}*/

            if(collidableObjects.get(i) == object){continue;}
            if(object.intersects(collidableObjects.get(i))) {
                onCollide(collidableObjects.get(i));
            }
        }

    }

    @Override
    public void renderUI(Graphics g) {

    }
}