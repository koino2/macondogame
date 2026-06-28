package game.scripts.misc.pause;

import lib.Object2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MenuItem extends Object2D {
    String name;

    MenuItem itemParent;
    List<MenuItem> menuChildren = new ArrayList<>();

    Runnable action;

    FontMetrics fontMetrics;

    Font font = new Font("Roboto Mono", Font.PLAIN, 30);

    public void rebuildTexture(){
        BufferedImage texture = new BufferedImage((int) xSize, (int) ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = texture.createGraphics();

        g.setFont(font);

        fontMetrics = g.getFontMetrics(font);

        new Object2D(0, 0, fontMetrics.stringWidth(name), fontMetrics.getHeight(), 0);

        g.setColor(Color.BLUE);
        g.drawString(name, 0, fontMetrics.getHeight());

        this.texture = texture;

        g.dispose();
    }

    public MenuItem(String name, Runnable action){
        super(0, 0, 10, 10, 0); // java stuff.
        this.name = name;
        this.action = action;

        rebuildTexture();
    }

    public void addMenuChild(MenuItem child){
        child.itemParent = this;
        menuChildren.add(child);
    }

    public int computeYSize(){
        return itemParent.menuChildren.size() * fontMetrics.getHeight();
    }

    public int xDiff = 200;

    public void addSubMenus(){
        for (int i = 0; i < menuChildren.size(); i++) {
            menuChildren.get(i).xPos = xDiff;
            menuChildren.get(i).yPos = (0-(menuChildren.get(0).computeYSize()/2))+(i*menuChildren.get(0).fontMetrics.getHeight());
            addChild(menuChildren.get(i));
        }
    }
}
