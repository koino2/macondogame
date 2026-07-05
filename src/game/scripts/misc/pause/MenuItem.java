package game.scripts.misc.pause;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {

    public String name;

    public MenuItem parentMenu;

    List<MenuItem> subMenus = new ArrayList<>();

    public Runnable action;

    public MenuItemVisual visual;

    public void addSubMenu(MenuItem item){
        subMenus.add(item);
        item.parentMenu = this;
    }

    public MenuItem(String name) {
        this.name = name;
    }
}
