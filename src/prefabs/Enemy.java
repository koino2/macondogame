package prefabs;

import lib.Object2D;
import lib.Script;
import lib.StaticTextures;
import scripts.CollisionScript;
import scripts.EnemyScript;

import java.awt.*;

public class Enemy extends Object2D {
    public int maxHealth = 100;
    public int health = maxHealth;

    static int width = 100;
    static int height = 100;

    public CollisionScript collisionScript;
    public EnemyScript enemyScript;

    public Enemy(int x, int y, int rotation){
        super(x, y, width, height, rotation);
        this.texture = StaticTextures.square();
        this.color = new Color(255, 39, 39);

        collisionScript = new CollisionScript() {
            @Override
            public void onCollide(Object2D other) {
                if(other instanceof Bullet){
                    health -= (int) ((Bullet) other).damage;
                    if(health <= 0){
                        destroy();
                    }
                    other.destroy();
                }
            }
        };
        collisionScript.collidableTags.add("bullet");
        collisionScript.collidableTags.add("wall");
        tags.add("enemy");
        addScript(collisionScript);

        enemyScript = new EnemyScript();
        addScript(enemyScript);
    }
}
