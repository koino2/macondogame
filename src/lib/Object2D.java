package lib;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class Object2D {
    public int xPos;
    public int yPos;
    public int xSize;
    public int ySize;
    public float rotation;

    public BufferedImage texture;
    public Color color = new Color(255, 255, 255);
    public int zIndex = 0;

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
        g.drawImage(tinted, -xSize/2, -ySize/2, xSize, ySize, null);
        g.setTransform(old);
    }
}
