package game.scripts.misc.pause;

import lib.Object2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuItemVisual extends Object2D {
    public MenuItem item;
    Font font = new Font("Segoe UI", Font.PLAIN, 50);

    BufferedImage sacrificialAnode = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D fg = sacrificialAnode.createGraphics();

    FontMetrics fm;

    public int textureResMultiplier = 1;

    public MenuItemVisual(MenuItem item) {
        super(0, 0, 1, 1, 0);

        this.item = item;
        item.visual = this;

        refreshTexture();
    }

    public void setFont (Font font){
        this.font = font;
        refreshTexture();
    }

    public Color textColor = new Color(255, 255, 255);

    public void refreshTexture() {

        Font scaledFont = font.deriveFont(font.getSize2D()*textureResMultiplier);

        fg.setFont(scaledFont);
        fm = fg.getFontMetrics();

        BufferedImage img = new BufferedImage(fm.stringWidth(item.name), fm.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /*g.setColor(Color.BLUE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());*/

        g.setColor(textColor);

        g.setFont(scaledFont);
        g.drawString(item.name, 0, fm.getAscent());
        this.texture = img;

        xSize = (float) fm.stringWidth(item.name) / textureResMultiplier;
        ySize = (float) fm.getAscent() / textureResMultiplier;

        g.dispose();
    }
}
