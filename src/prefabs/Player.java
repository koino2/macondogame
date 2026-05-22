package prefabs;

import lib.Object2D;
import lib.Script;
import scripts.CollisionScript;
import scripts.PlayerController;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Player extends Object2D {
    static int width = 100;
    static int height = 100;

    public int maxHealth = 100;
    public int health = maxHealth;
    public PlayerController playerControllerScript;
    public CollisionScript collisionScript;
    public Player(float xPos, float yPos, float rotation){
        super(xPos, yPos, width, height, rotation);
        try {
            texture = ImageIO.read(new File("src/assets/player.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        playerControllerScript = new PlayerController();
        addScript(playerControllerScript);
        collisionScript = new CollisionScript() {
            @Override
            public void onCollide(Object2D other) {
                if(other instanceof Enemy){
                    health -= 10;
                    System.out.println(health);
                }
                resolveCollision(other);
            }
        };
        addScript(collisionScript);
    }
}
