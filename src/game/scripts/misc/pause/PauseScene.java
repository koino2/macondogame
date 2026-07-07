package game.scripts.misc.pause;

import game.scripts.player.CameraController;
import game.scripts.ui.DebugText;
import lib.*;
import lib.postProcessEffects.Bloom;
import lib.postProcessEffects.Vignette;

import java.awt.*;
import java.awt.event.KeyEvent;
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
    Font defaultFont = new Font("Segoe UI", Font.PLAIN, 300);
    Font selectedFont = new Font("Segoe UI", Font.BOLD, 300);

    public void showChildren(MenuItem item){
        int y = 0;
        for (MenuItem child : item.subMenus) {
            MenuItemVisual visual = new MenuItemVisual(child);

            if (item.visual != null) {
                visual.xPos = item.visual.xPos + item.visual.texture.getWidth() + 100;
            } else {
                visual.xPos = 200;
            }
            visual.yPos = y;

            addObject(visual);

            y += 100;
        }
    }

    public void hideChildren(MenuItem parent){
        for (MenuItem child : parent.subMenus){
            if (child.visual != null) {
                child.visual.destroy();
                child.visual = null;
            }
        }
    }

    @Override
    public void start() {

        try (InputStream is = getClass().getResourceAsStream("/assets/fonts/Bitcount.ttf")) {
            base = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(100f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        defaultFont = base.deriveFont(Font.ITALIC);
        selectedFont = base.deriveFont(Font.BOLD);

        root = new MenuItem("bruhpoo");

        MenuItem numbers = new MenuItem("Numbers");
        numbers.addSubMenu(new MenuItem("one"));
        numbers.addSubMenu(new MenuItem("two"));
        numbers.addSubMenu(new MenuItem("three"));
        numbers.addSubMenu(new MenuItem("four"));
        root.addSubMenu(numbers);

        MenuItem alphabets = new MenuItem("Alphabets");
        alphabets.addSubMenu(new MenuItem("a"));
        alphabets.addSubMenu(new MenuItem("b"));
        alphabets.addSubMenu(new MenuItem("c"));
        alphabets.addSubMenu(new MenuItem("d"));
        alphabets.addSubMenu(new MenuItem("e"));
        alphabets.addSubMenu(new MenuItem("f"));
        alphabets.addSubMenu(new MenuItem("g"));
        alphabets.addSubMenu(new MenuItem("h"));
        root.addSubMenu(alphabets);

        this.pointer = root.subMenus.get(0);

        showChildren(root);

        pointer.visual.setFont(selectedFont);

        Camera camera = new Camera(0, 0, 0);
        this.camera = camera;
        cameraController = new CameraController(pointer.visual);
        camera.addScript(cameraController);
        addObject(camera);

        Object2D background = new Object2D(0, 0, 1, 1, 0);
        background.zIndex = -100;
        background.color = new Color(78, 34, 47);
        background.addScript(new Script() {
            @Override
            public void start() {

            }

            @Override
            public void update(double deltaTime) {
                object.xSize = object.scene.engine.getWidth();
                object.ySize = object.scene.engine.getHeight();
            }
        });
        cameraController.maxZoom = 1;
        cameraController.minZoom = 1;
        camera.addChild(background);

        BackgroundScript bg = new BackgroundScript();
        background.addScript(bg);

        Bloom bloom = new Bloom();
        bloom.threshold = 0.9f;
        postProcessEffects.add(bloom);

        Vignette vignette = new Vignette();
        postProcessEffects.add(vignette);

        camera.addScript(new DebugText());
    }

    public void sound1(){
        Sound sound = new Sound("src/assets/audio/ui/ui-navigate-1.wav");
        camera.sounds.add(sound);
        sound.play();
    }

    public void sound2(){
        Sound sound = new Sound("src/assets/audio/ui/ui-navigate-2.wav");
        camera.sounds.add(sound);
        sound.play();
    }

    public void sound3(){
        Sound sound = new Sound("src/assets/audio/ui/ui-navigate-3.wav");
        camera.sounds.add(sound);
        sound.play();
    }

    public void onMove(MenuItem previous){
        cameraController.target = pointer.visual;
        if (previous.visual == null) return;
        previous.visual.setFont(defaultFont);
        pointer.visual.setFont(selectedFont);
    }

    public void moveUp(){
        if (pointer.parentMenu == null) return;

        int index = pointer.parentMenu.subMenus.indexOf(pointer);

        if (index > 0){
            MenuItem previous = pointer;
            pointer = pointer.parentMenu.subMenus.get(index-1);

            onMove(previous);
            sound2();
        }
    }

    public void moveDown(){
        if (pointer.parentMenu == null) return;

        int index = pointer.parentMenu.subMenus.indexOf(pointer);

        if (index < pointer.parentMenu.subMenus.size() -1){
            MenuItem previous = pointer;
            pointer = pointer.parentMenu.subMenus.get(index + 1);

            sound2();
            onMove(previous);
        }
    }

    public void enterMenu(){
        if (pointer.subMenus.isEmpty()) return;

        //hideChildren(pointer.parentMenu);

        MenuItem previous = pointer;
        pointer = pointer.subMenus.get(0);

        showChildren(previous);

        sound1();
        onMove(previous);
    }

    public void exitMenu(){
        if (pointer.parentMenu == null || pointer.parentMenu == root) return;

        hideChildren(pointer.parentMenu);

        MenuItem previous = pointer;
        pointer = pointer.parentMenu;

        //showChildren(pointer.parentMenu);

        sound1();
        onMove(previous);
    }

    public void action(){
        sound3();
        if (pointer.action != null) {
            pointer.action.run();
        }
    }

    public void updateFonts(MenuItem item){
        for (MenuItem child : item.subMenus) {

            if (child.visual != null)
                if (child == pointer)
                    child.visual.setFont(selectedFont);
                else
                    child.visual.setFont(defaultFont);

            updateFonts(child);
        }
    }

    @Override
    public void update(double deltaTime) {

        updateFonts(root);

        if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)){
            engine.changeScene(unpauseScene);
        }

        if (Input.isKeyPressed(KeyEvent.VK_W)){
            moveUp();
        }

        if (Input.isKeyPressed(KeyEvent.VK_S)) {
            moveDown();
        }

        if (Input.isKeyPressed(KeyEvent.VK_A)){
            exitMenu();
        }

        if (Input.isKeyPressed(KeyEvent.VK_D)){
            enterMenu();
        }

        if (Input.isKeyPressed(KeyEvent.VK_SPACE)){
            action();
        }

    }

    public Color bgColor = new Color(44, 52, 71);

    @Override
    public void renderUI(Graphics g) {
        g.setColor(bgColor);
        //g.fillRect(0, 0, engine.getWidth(), engine.getHeight());
    }
}
