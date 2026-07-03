package game.scripts.misc.pause;

import game.scripts.player.CameraController;
import lib.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;

public class PauseScene extends Scene {

    public Scene unpauseScene;

    public PauseScene(Scene scene){
        this.unpauseScene = scene;
    }

    MenuItem pointer;

    CameraController cameraController;

    MenuItem root;

    Font base;
    Font defaultFont = new Font("Segoe UI", Font.PLAIN, 50);
    Font selectedFont = new Font("Segoe UI", Font.BOLD, 50);

    @Override
    public void start() {

        try (InputStream is = getClass().getResourceAsStream("/assets/fonts/Bitcount.ttf")) {
            base = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(50f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        defaultFont = base.deriveFont(Font.PLAIN);
        selectedFont = base.deriveFont(Font.BOLD);

        root = new MenuItem("bruhpoo");

        root.addSubMenu(new MenuItem("one"));
        root.addSubMenu(new MenuItem("two"));
        root.addSubMenu(new MenuItem("three"));
        root.addSubMenu(new MenuItem("four"));

        root.rendered = false;
        root.refreshTexture();

        addObject(root);

        this.pointer = root.subMenus.get(0);
        root.addChildren();

        Camera camera = new Camera(0, 0, 0);
        this.camera = camera;
        cameraController = new CameraController(root.subMenus.get(0));
        camera.addScript(cameraController);
        addObject(camera);
    }

    public void onMove(MenuItem previous){
        cameraController.target = pointer;
        previous.setFont(defaultFont);
        pointer.setFont(selectedFont);
    }

    @Override
    public void update(double deltaTime) {

        if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)){
            engine.changeScene(unpauseScene);
        }

        if (Input.isKeyPressed(KeyEvent.VK_A)){

            MenuItem previous = pointer;

            if (pointer.parentMenu != null && pointer.parentMenu != root){
                pointer = pointer.parentMenu;

                Sound sound = new Sound("src/assets/audio/ui/ui-navigate-1.wav");
                camera.sounds.add(sound);
                sound.play();
            }
            pointer.removeSubMenus();

            onMove(previous);

        }
        if (Input.isKeyPressed(KeyEvent.VK_D)){

            MenuItem previous = pointer;

            pointer.addChildren();
            if (!pointer.subMenus.isEmpty()){
                pointer = pointer.subMenus.get(0);

                Sound sound = new Sound("src/assets/audio/ui/ui-navigate-1.wav");
                camera.sounds.add(sound);
                sound.play();
            }

            onMove(previous);

        }
        if (Input.isKeyPressed(KeyEvent.VK_W)){

            MenuItem previous = pointer;

            if (pointer.parentMenu != null) {
                int index = pointer.parentMenu.subMenus.indexOf(pointer);

                if (index != 0 && index < pointer.parentMenu.subMenus.size()) {
                    pointer = pointer.parentMenu.subMenus.get(index - 1);

                    Sound sound = new Sound("src/assets/audio/ui/ui-navigate-2.wav");
                    camera.sounds.add(sound);
                    sound.play();
                }
            }

            onMove(previous);
        }
        if (Input.isKeyPressed(KeyEvent.VK_S)){

            MenuItem previous = pointer;

            if (pointer.parentMenu != null) {
                int index = pointer.parentMenu.subMenus.indexOf(pointer);

                if (pointer.parentMenu.subMenus.size() > index+1) {
                    pointer = pointer.parentMenu.subMenus.get(index + 1);

                    Sound sound = new Sound("src/assets/audio/ui/ui-navigate-2.wav");
                    camera.sounds.add(sound);
                    sound.play();
                }
            }

            onMove(previous);
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
