package lib;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class Object2D {
    public float xPos;
    public float yPos;
    public float xSize;
    public float ySize;
    public float rotation;

    public BufferedImage texture = StaticTextures.square();
    public Color color = new Color(255, 255, 255);
    public int zIndex = 0;

    public float xVelocity;
    public float yVelocity;
    public float xAcceleration;
    public float yAcceleration;

    public void update(double deltaTime){
        xVelocity += (float) (xAcceleration * deltaTime);
        yVelocity += (float) (yAcceleration * deltaTime);

        xPos += (float) (xVelocity * deltaTime);
        yPos += (float) (yVelocity * deltaTime);
    }

    public Object2D(float xPos, float yPos, float xSize, float ySize, float rotation){
        this.xPos = xPos;
        this.yPos = yPos;
        this.xSize = xSize;
        this.ySize = ySize;
        this.rotation = rotation;
    }

    public void render(Graphics2D g){
        AffineTransform old = g.getTransform();
        g.translate(xPos,yPos);
        g.rotate(Math.toRadians(rotation));
        RescaleOp op = new RescaleOp(
                new float[]{(float) color.getRed() /255, (float) color.getGreen() /255, (float) color.getBlue() /255, (float) color.getAlpha() /255},
                new float[4],
                null
        );
        BufferedImage tinted = op.filter(texture, null);
        g.drawImage(tinted, (int) (-xSize/2), (int) (-ySize/2), (int) xSize, (int) ySize, null);
        g.setTransform(old);
    }

    public boolean intersects(Object2D other){
        Point2D.Float[] a = this.getVertices();
        Point2D.Float[] b = other.getVertices();

        return checkAxes(a,b) && checkAxes(b,a);
    }

    // ugly sat collision detection stuff
    // |  |  |  |  |  |  |  |  |  |  |  |
    // V  V  V  V  V  V  V  V  V  V  V  V

    float[] project(Point2D.Float[] verts, float axisX, float axisY) {
        float min = dot(verts[0], axisX, axisY);
        float max = min;

        for (int i = 1; i < verts.length; i++) {
            float p = dot(verts[i], axisX, axisY);
            if(p < min){
                min = p;
            }
            if(p > max){
                max = p;
            }
        }

        return new float[]{min,max};
    }

    float dot(Point2D.Float p, float ax, float ay){
        return  p.x * ax + p.y * ay;
    }

    public Point2D.Float[] getVertices(){
        float halfWidth = xSize/2;
        float halfHeight = ySize/2;

        float[][] local = {
                {-halfWidth, -halfHeight},
                { halfWidth, -halfHeight},
                { halfWidth,  halfHeight},
                {-halfWidth,  halfHeight}
        };

        float rad = (float) Math.toRadians(rotation);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        Point2D.Float[] vertices = new Point2D.Float[4];

        for (int i = 0; i < local.length; i++) {
            float localX = local[i][0];
            float localY = local[i][1];

            float rx = localX * cos - localY * sin;
            float ry = localX * sin + localY * cos;

            vertices[i] = new Point2D.Float(rx + xPos, ry + yPos);
        }

        return vertices;
    }

    public boolean checkAxes(Point2D.Float[] a, Point2D.Float[] b){
        for (int i = 0; i < 4; i++) {
            Point2D.Float p1 = a[i];
            Point2D.Float p2 = a[(i+1)%4];

            float edgeX = p2.x - p1.x;
            float edgeY = p2.y - p1.y;

            float axisX = -edgeY;
            float axisY = edgeX;

            float[] projA = project(a, axisX, axisY);
            float[] projB = project(b, axisX, axisY);

            if(projA[1] < projB[0] || projB[1] < projA[0]){
                return false;
            }
        }
        return true;
    }
}
