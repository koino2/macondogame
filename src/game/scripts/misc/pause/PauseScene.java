package game.scripts.misc.pause;

import game.scripts.player.CameraController;
import lib.Camera;
import lib.Input;
import lib.Object2D;
import lib.Scene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PauseScene extends Scene {

    public Scene unpauseScene;

    public PauseScene(Scene scene){
        this.unpauseScene = scene;
    }

    MenuState state = new MenuState();

    @Override
    public void start() {
        MenuItem root = new MenuItem();
        root.name = "Main Menu";

        MenuItem play = new MenuItem();
        play.name = "Play";
        play.parent = root;

        MenuItem settings = new MenuItem();
        settings.name = "Settings";
        settings.parent = root;

        MenuItem exit = new MenuItem();
        exit.name = "Exit";
        exit.parent = root;

        root.children.add(play);
        root.children.add(settings);
        root.children.add(exit);

        state.root = root;
        state.current = play;

        Camera camera = new Camera(0, 0, 0);
        this.camera = camera;
        CameraController cc = new CameraController(new Object2D(0, 0, 0, 0, 0));
        camera.addScript(cc);
    }

    @Override
    public void update(double deltaTime) {

        if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)){
            engine.changeScene(unpauseScene);
        }

        List<VisibleNode> visible = new ArrayList<>();
        int index = -1;
        for (int i = 0; i < visible.size(); i++) {
            if (visible.get(i).item == state.current){
                index = i;
                break;
            }
        }

        if (Input.isKeyPressed(KeyEvent.VK_S)){
            if (index > visible.size()-1){
                state.current = visible.get(index+1).item;
            }
        }
        if (Input.isKeyPressed(KeyEvent.VK_W)){
            if (index > 0){
                state.current = visible.get(index+1).item;
            }
        }
        if (Input.isKeyPressed(KeyEvent.VK_D)){
            if (!state.current.children.isEmpty()){
                state.current.expanded = true;
            }
        }
        if (Input.isKeyPressed(KeyEvent.VK_A)){
            if (state.current.parent != null){
                state.current = state.current.parent;
            }
        }
        if (Input.isKeyPressed(KeyEvent.VK_SPACE)){
            System.out.println(state.current.name);
            if (state.current.action != null){
                state.current.action.run();
            }
        }
    }

    public Color bgColor = new Color(44, 52, 71);

    @Override
    public void renderUI(Graphics g) {
        g.setColor(bgColor);
        g.fillRect(0, 0, engine.getWidth(), engine.getHeight());

        MenuRenderer renderer = new MenuRenderer();

        List<VisibleNode> visible = renderer.buildVisible(state.root);

        g.setColor(Color.BLUE);

        renderer.render(g, visible, state.current);
    }
}
