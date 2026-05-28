package prefabs;

import levels.Level1;
import levels.SampleScene;
import lib.Object2D;
import lib.Scene;
import lib.Script;
import lib.postProcessEffects.Vignette;
import scripts.CollisionScript;
import scripts.HealthScript;
import scripts.PlayerController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Player extends Object2D {
    static int width = 100;
    static int height = 100;

    public PlayerController playerControllerScript;
    public CollisionScript collisionScript;
    public HealthScript healthScript;

    public void onDeath() {}

    public Player(float xPos, float yPos, float rotation){

        super(xPos, yPos, width, height, rotation);
        try {
            texture = ImageIO.read(new File("src/assets/player.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        playerControllerScript = new PlayerController();
        addScript(playerControllerScript);

        healthScript = new HealthScript() {
            Vignette healthVignette;
            @Override
            public void start() {
                healthVignette = new Vignette();
                healthVignette.strength = 0;
                healthVignette.color = Color.RED;
                object.scene.postProcessEffects.add(healthVignette);
            }

            @Override
            public void behaviour(double deltaTime) {
                if (healthVignette.strength > 0) {
                    healthVignette.strength -= (float) deltaTime;
                    if(healthVignette.strength < 0){healthVignette.strength = 0;}
                }
                if (healthVignette.radiusMultiplier < 1) {
                    healthVignette.radiusMultiplier += (float) deltaTime*0.1f;
                }
            }

            @Override
            public void onDamage(float damageAmount) {
                healthVignette.strength += 1f;
                healthVignette.radiusMultiplier -= 0.1f;

                if(health <= 0){
                    onDeath();
                }
            }

            @Override
            public void renderUI(Graphics g) {
                int width = object.scene.engine.getWidth();
                int height = object.scene.engine.getHeight();

                int barWidth = 400;
                int barHeight = 25;

                int xPos = 100;
                int yPos = (int) (height*0.95f);

                g.setColor(new Color(101, 26, 57));
                g.fillRect(95, yPos-(barHeight+5), barWidth+10, barHeight+10);

                g.setColor(new Color(33, 63, 76));
                g.fillRect(100, yPos-barHeight, barWidth, barHeight);
                g.setColor(new Color(255, 94, 94));
                g.fillRect(100, yPos-barHeight, (int) (barWidth*(health/maxHealth)), barHeight);
            }
        };
        addScript(healthScript);

        collisionScript = new CollisionScript() {
            @Override
            public void onCollide(Object2D other) {
                if(!other.tags.contains("noCollision") && !other.tags.contains("bullet")) {
                    resolveCollision(other);
                }
            }
        };
        addScript(collisionScript);
    }
}
