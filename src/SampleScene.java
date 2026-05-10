import lib.*;
import lib.postProcessEffects.Bloom;
import lib.postProcessEffects.ChromaticAberration;
import lib.postProcessEffects.Vignette;
import scripts.CollisionScript;
import scripts.FPSTimer;
import scripts.PlayerController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SampleScene extends Scene {
    Object2D player;
    Object2D block;

    @Override
    public void update(double deltaTime) {
        lights.get(0).x = player.xPos;
        lights.get(0).y = player.yPos;
    }

    @Override
    public void renderUI(Graphics g){

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

        player.addScript(new FPSTimer());
        player.addScript(new PlayerController());
        CollisionScript collisionScript = new CollisionScript();
        collisionScript.collidableObjects.add(block);
        player.addScript(collisionScript);

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
        Light light = new Light();
        light.x = player.xPos;
        light.y = player.yPos;
        light.color = new Color(255, 255, 255, 140);
        light.radius = 200;
        lights.add(light);

        Vignette vignette = new Vignette();
        vignette.size = 0.1f;
        vignette.priority = 999;
        postProcessEffects.add(vignette);

        Bloom bloom = new Bloom();
        bloom.threshold = 0.3f;
        bloom.radius = 3;
        postProcessEffects.add(bloom);

        ChromaticAberration chromaticAberration = new ChromaticAberration();
        postProcessEffects.add(chromaticAberration);
    }
}
