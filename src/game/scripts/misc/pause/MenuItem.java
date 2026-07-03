package game.scripts.misc.pause;

import lib.Object2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MenuItem extends Object2D {

    BufferedImage kakoos = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Graphics fontG = kakoos.createGraphics();

    public Font font = new Font("Segoe UI", Font.PLAIN, 50);
    FontMetrics fm;

    public String name;

    Color color = new Color(0, 0, 0);

    public MenuItem parentMenu;

    List<MenuItem> subMenus = new ArrayList<>();

    public boolean rendered = true;

    public void addSubMenu(MenuItem item){
        subMenus.add(item);
        item.parentMenu = this;
    }

    Object2D item;

    public void addChildren(){
        for (int i = 0; i < subMenus.size(); i++) {
            subMenus.get(i).xPos = 200;
            if (i != 0 && subMenus.size() > 1){
                subMenus.get(i).yPos = subMenus.get(i-1).yPos + 100;
            }
            addChild(subMenus.get(i));

            item = new Object2D(
                    subMenus.get(i).xPos,
                    subMenus.get(i).yPos,
                    subMenus.get(i).xSize,
                    subMenus.get(i).ySize,
                    subMenus.get(i).rotation
            );
            item.texture = subMenus.get(i).texture;

            addChild(item);
        }
    }

    public void removeSubMenus(){
        for (int i = 0; i < children.size(); i++) {
            children.get(i).destroy();
        }
        children.clear();
    }

    void refreshFontMetrics(){
        fontG.setFont(font);
        fm = fontG.getFontMetrics();
    }

    public void refreshTexture(){
        if (rendered) {
            BufferedImage img = new BufferedImage(fm.stringWidth(name), fm.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();

            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(color);
            g.setFont(font);
            g.drawString(name, 0, fm.getAscent());
            this.texture = img;

            xSize = fm.stringWidth(name);
            ySize = fm.getAscent();
        } else{
            this.texture = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
    }

    public void setFont(Font font){
        this.font = font;
        refreshFontMetrics();
        refreshTexture();
    }

    public void setText(String text){
        this.name = text;
        refreshTexture();
    }

    public MenuItem(String name) {
        super(0, 0, 1, 1, 0);
        refreshFontMetrics();
        setText(name);
    }
}
