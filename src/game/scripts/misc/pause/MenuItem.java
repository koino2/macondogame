package game.scripts.misc.pause;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
    String name;
    Runnable action;

    MenuItem parent;
    List<MenuItem> children = new ArrayList<>();

    boolean expanded;
}
