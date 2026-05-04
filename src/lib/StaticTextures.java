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
}
