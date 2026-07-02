package game.scripts.misc.pause;

import game.scripts.player.CameraController;
import lib.Camera;
import lib.Input;
import lib.Object2D;
import lib.Scene;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseScene extends Scene {

    public Scene unpauseScene;

    public PauseScene(Scene scene){
        this.unpauseScene = scene;
    }

    MenuItem pointer;

    @Override
    public void start() {

        MenuItem root = new MenuItem("bruhpoo");

        root.addSubMenu(new MenuItem("one"));
        root.addSubMenu(new MenuItem("two"));
        root.addSubMenu(new MenuItem("three"));
        root.addSubMenu(new MenuItem("four"));

        addObject(root);

        this.pointer = root;

        Camera camera = new Camera(0, 0, 0);
        this.camera = camera;
        CameraController cc = new CameraController(new Object2D(0, 0, 0, 0, 0));
        camera.addScript(cc);
        addObject(camera);
    }

    @Override
    public void update(double deltaTime) {

        if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)){
            engine.changeScene(unpauseScene);
        }

        if (Input.isKeyPressed(KeyEvent.VK_A)){
            if (pointer.parentMenu != null){
                pointer = pointer.parentMenu;
            }
            pointer.removeSubMenus();
        }
        if (Input.isKeyPressed(KeyEvent.VK_D)){
            pointer.addChildren();
            if (!pointer.subMenus.isEmpty()){
                pointer = pointer.subMenus.get(0);
            }
        }
        if (Input.isKeyPressed(KeyEvent.VK_W)){
            if (pointer.parentMenu != null) {
                int index = pointer.parentMenu.subMenus.indexOf(pointer);

                if (index != 0 && index < pointer.parentMenu.subMenus.size()) {
                    pointer = pointer.parentMenu.subMenus.get(index - 1);
                }
            }
        }
        if (Input.isKeyPressed(KeyEvent.VK_S)){
            if (pointer.parentMenu != null) {
                int index = pointer.parentMenu.subMenus.indexOf(pointer);

                if (pointer.parentMenu.subMenus.size() > index+1) {
                    pointer = pointer.parentMenu.subMenus.get(index + 1);
                }
            }
        }

        if (Input.isKeyPressed(KeyEvent.VK_SPACE)){
            System.out.println(pointer.name);
        }
    }

    public Color bgColor = new Color(44, 52, 71);

    @Override
    public void renderUI(Graphics g) {
        g.setColor(bgColor);
        //g.fillRect(0, 0, engine.getWidth(), engine.getHeight());
    }
}
