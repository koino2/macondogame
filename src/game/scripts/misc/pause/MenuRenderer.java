package game.scripts.misc.pause;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuRenderer {
    List<VisibleNode> buildVisible(MenuItem root){
        List<VisibleNode> out = new ArrayList<>();
        dfs(root, 0, out);
        return out;
    }
    public void dfs(MenuItem node, int depth, List<VisibleNode> out){
        out.add(new VisibleNode(node, depth));
        if (!node.expanded) return;
        for (MenuItem c : node.children){
            dfs(c, depth+1, out);
        }
    }
    public void render(Graphics g, List<VisibleNode> list, MenuItem selected){
        int y = 50;
        for (VisibleNode v : list){
            int x = 50 + v.depth * 25;
            if (v.item == selected){
                g.drawRect(x-10, y, 200, 25);
            }
            g.drawString(v.item.name, x, y);
            y += 30;
        }
    }
}
