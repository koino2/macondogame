package game.scripts.misc.pause;

import lib.Input;
import lib.Scene;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseScene extends Scene {

    public Scene unpauseScene;

    public MenuItem menu = new MenuItem("root", null);

    public MenuItem selected;

    public PauseScene(Scene scene){
        this.unpauseScene = scene;
    }

    @Override
    public void start() {
        menu.addMenuChild(new MenuItem("Resume", () -> {System.exit(0);}));
        menu.addMenuChild(new MenuItem("numbers", null));
        menu.menuChildren.get(0).addMenuChild(new MenuItem("1", null));
        menu.menuChildren.get(0).addMenuChild(new MenuItem("2", null));
        menu.menuChildren.get(0).addMenuChild(new MenuItem("3", null));
        menu.menuChildren.get(0).addMenuChild(new MenuItem("4", null));
        menu.addMenuChild(new MenuItem("alphabet", null));
        menu.menuChildren.get(1).addMenuChild(new MenuItem("a", null));
        menu.menuChildren.get(1).addMenuChild(new MenuItem("b", null));
        menu.menuChildren.get(1).addMenuChild(new MenuItem("c", null));
        menu.menuChildren.get(1).addMenuChild(new MenuItem("d", null));

        addObject(menu);
        menu.addSubMenus();
    }

    @Override
    public void update(double deltaTime) {

        if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)){
            engine.changeScene(unpauseScene);
        }

    }

    public Color bgColor = new Color(44, 52, 71);
    @Override
    public void renderUI(Graphics g) {
        g.setColor(bgColor);
        //g.fillRect(0, 0, engine.getWidth(), engine.getHeight());
    }
}
