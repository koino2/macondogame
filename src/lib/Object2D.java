package lib;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class Object2D {
    public float xPos;
    public float yPos;
    public float xSize;
    public float ySize;
    public float rotation;

    public BufferedImage texture;
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

    public Object2D(int xPos, int yPos, int xSize, int ySize, float rotation){
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
}
