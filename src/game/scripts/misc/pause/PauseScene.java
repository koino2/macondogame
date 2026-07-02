package game.scripts.misc.pause;

import game.scripts.player.CameraController;
import lib.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseScene extends Scene {

    public Scene unpauseScene;

    public PauseScene(Scene scene){
        this.unpauseScene = scene;
    }

    MenuItem pointer;

    CameraController cameraController;

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
        cameraController = new CameraController(new Object2D(0, 0, 0, 0, 0));
        camera.addScript(cameraController);
        addObject(camera);
    }

    public void onMove(){
        cameraController.target = pointer;
    }

    @Override
    public void update(double deltaTime) {

        if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)){
            engine.changeScene(unpauseScene);
        }

        if (Input.isKeyPressed(KeyEvent.VK_A)){

            if (pointer.parentMenu != null){
                pointer = pointer.parentMenu;

                Sound sound = new Sound("src/assets/audio/ui/ui-navigate-1.wav");
                camera.sounds.add(sound);
                sound.play();
            }
            pointer.removeSubMenus();

            onMove();

        }
        if (Input.isKeyPressed(KeyEvent.VK_D)){

            pointer.addChildren();
            if (!pointer.subMenus.isEmpty()){
                pointer = pointer.subMenus.get(0);

                Sound sound = new Sound("src/assets/audio/ui/ui-navigate-1.wav");
                camera.sounds.add(sound);
                sound.play();
            }

            onMove();

        }
        if (Input.isKeyPressed(KeyEvent.VK_W)){
            if (pointer.parentMenu != null) {
                int index = pointer.parentMenu.subMenus.indexOf(pointer);

                if (index != 0 && index < pointer.parentMenu.subMenus.size()) {
                    pointer = pointer.parentMenu.subMenus.get(index - 1);

                    Sound sound = new Sound("src/assets/audio/ui/ui-navigate-2.wav");
                    camera.sounds.add(sound);
                    sound.play();
                }
            }

            onMove();
        }
        if (Input.isKeyPressed(KeyEvent.VK_S)){
            if (pointer.parentMenu != null) {
                int index = pointer.parentMenu.subMenus.indexOf(pointer);

                if (pointer.parentMenu.subMenus.size() > index+1) {
                    pointer = pointer.parentMenu.subMenus.get(index + 1);

                    Sound sound = new Sound("src/assets/audio/ui/ui-navigate-2.wav");
                    camera.sounds.add(sound);
                    sound.play();
                }
            }

            onMove();
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
