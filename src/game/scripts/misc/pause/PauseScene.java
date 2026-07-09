package game.scripts.misc.pause;

import game.scripts.misc.Settings;
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

    public Settings sceneSettings;
    public Settings settings;

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

        Object2D scripts = new Object2D(0, 0, 0, 0, 0);
        settings = new Settings();
        scripts.addScript(settings);
        addObject(scripts);

        root = new MenuItem("bruhpoo");

        MenuItem resume = new MenuItem("Resume");
        resume.action = () -> engine.changeScene(unpauseScene);;
        root.addSubMenu(resume);

        MenuItem settingsMenu = new MenuItem("Settings");

        MenuItem audio = new MenuItem("Audio");

        MenuItem volumeLabel = new MenuItem("Volume: "+(int)(Settings.volume*100));
        MenuItem increase = new MenuItem("+");
        increase.action = () -> {
            settings.setVolume(((Settings.volume *100)+10)/100);
            volumeLabel.name = "Volume: "+((int)(Settings.volume*100));
            volumeLabel.visual.refreshTexture();
        };
        MenuItem decrease = new MenuItem("-");
        decrease.action = () -> {
            settings.setVolume(((Settings.volume *100)-10)/100);
            volumeLabel.name = "Volume: "+((Settings.volume*100));
            volumeLabel.visual.refreshTexture();
        };
        audio.addSubMenu(increase);
        audio.addSubMenu(volumeLabel);
        audio.addSubMenu(decrease);

        settingsMenu.addSubMenu(audio);

        root.addSubMenu(settingsMenu);

        MenuItem quit = new MenuItem("Quit");
        quit.action = () -> System.exit(0);
        root.addSubMenu(quit);

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
        Sound sound = new Sound("src/assets/audio/ui/ui-navigate-1.wav", 1);
        camera.sounds.add(sound);
        sound.setVolume(1);
        sound.play();
    }

    public void sound2(){
        Sound sound = new Sound("src/assets/audio/ui/ui-navigate-2.wav", 1);
        camera.sounds.add(sound);
        sound.setVolume(1);
        sound.play();
    }

    public void sound3(){
        Sound sound = new Sound("src/assets/audio/ui/ui-navigate-3.wav", 1);
        camera.sounds.add(sound);
        sound.setVolume(1);
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

        sceneSettings.updateSettings();
        settings.updateSettings();
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
            sceneSettings.updateSettings();
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
