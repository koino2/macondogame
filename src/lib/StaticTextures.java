package lib;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StaticTextures {
    public static BufferedImage square(){
        BufferedImage img = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        img.getGraphics().setColor(Color.WHITE);
        img.getGraphics().fillRect(0,0,1,1);
        return img;
    }
    public static BufferedImage circle(int res){
        BufferedImage img = new BufferedImage(res, res, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE);
            g.fillOval(0, 0, res, res);
        } finally {
            g.dispose();
        }
        return img;
    }
}
