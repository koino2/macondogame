import lib.*;
import lib.postProcessEffects.*;
import scripts.CameraController;
import scripts.CollisionScript;
import scripts.DebugText;
import scripts.PlayerController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class SampleScene extends Scene {
    public static void main(String[] args) {
        JFrame window = new JFrame("Untitled Macondo Game");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1200, 700);
        window.setLocationRelativeTo(null);

        SampleScene scene = new SampleScene();
        Engine engine = new Engine(scene);
        window.setContentPane(engine);
        window.setVisible(true);
    }
    Object2D player;
    Object2D block;

    @Override
    public void update(double deltaTime) {

    }


    boolean before = false;
    @Override
    public void renderUI(Graphics g){
        if(Input.isKeyDown(KeyEvent.VK_B)){
            if(!before) {
                postProcessingEnabled = !postProcessingEnabled;
            }

            before = true;
        } else{
            before = false;
        }
    }

    @Override
    public void start() {
        player = new Object2D(200,200,100,100,0);
        try {
            player.texture = ImageIO.read(new File("src/assets/player.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addObject(player);

        block = new Object2D(500,500,200,200,45);
        block.color = new Color(117, 255, 117);
        block.zIndex = -1;
        addObject(block);

        player.addScript(new DebugText());
        player.addScript(new PlayerController());
        CollisionScript collisionScript = new CollisionScript() {
            @Override
            public void onCollide(Object2D other) {
                resolveCollision(other);
            }
        };
        collisionScript.collidableObjects.add(block);
        player.addScript(collisionScript);

        /*Color wallColor = new Color(46, 255, 239, 255);
        int wallZindex = 5;
        Object2D wall1 = new Object2D(0, 350, 50,  700, 0);
        wall1.color = wallColor;
        wall1.zIndex = wallZindex;
        addObject(wall1);
        collisionScript.collidableObjects.add(wall1);
        Object2D wall2 = new Object2D(1200, 350, 50,  700, 0);
        wall2.color = wallColor;
        wall2.zIndex = wallZindex;
        addObject(wall2);
        collisionScript.collidableObjects.add(wall2);*/

        Object2D bg = new Object2D(
                engine.getWidth()/2f,
                engine.getHeight()/2f,
                engine.getWidth(),
                engine.getHeight(),
                0
        );
        bg.color = new Color(255, 255, 255);
        bg.zIndex = -20;
        addObject(bg);

        ambientColor = new Color(45, 47, 62, 255);
        Light light = new Light(0, 0, 200);
        player.children.add(light);
        light.parent = player;
        light.color = new Color(255, 255, 255, 140);

        Vignette vignette = new Vignette();
        vignette.size = 0.1f;
        vignette.priority = 999;
        postProcessEffects.add(vignette); // -3 fps

        Bloom4 bloom = new Bloom4();
        bloom.threshold = 0.3f;
        bloom.radius = 2;
        bloom.reduction = 4;
        postProcessEffects.add(bloom); // -12

        ChromaticAberration chromaticAberration = new ChromaticAberration();
        postProcessEffects.add(chromaticAberration); //-3 fps

        Camera camera = new Camera(0,0, 0);
        camera.scale = 0.75f;
        camera.addScript(new CameraController(player));
        addObject(camera);
        this.camera = camera;
    }
}
