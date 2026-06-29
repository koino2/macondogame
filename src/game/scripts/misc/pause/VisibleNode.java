package game.scripts.misc.pause;

import java.util.ArrayList;
import java.util.List;

public class VisibleNode {
    MenuItem item;
    int depth;

    public VisibleNode(MenuItem node, int depth){
        this.item = node;
        this.depth = depth;
    }
}
