package game.prefabs;

import lib.*;
import lib.CollisionScript;
import game.scripts.enemies.EnemyScript;
import game.scripts.misc.HealthScript;

import java.awt.*;

public class Enemy extends Object2D {
    static int width = 100;
    static int height = 100;

    public CollisionScript collisionScript;
    public EnemyScript enemyScript;
    public HealthScript healthScript;

    public Enemy(int x, int y, int rotation){
        super(x, y, width, height, rotation);
        this.texture = StaticTextures.square();
        this.color = new Color(255, 39, 39);

        healthScript = new HealthScript() {
            @Override
            public void onDamage(float damageAmount) {
                if (health <= 0) {
                    destroy();
                }
            }
        };
        addScript(healthScript);

        collisionScript = new CollisionScript() {
            @Override
            public void onCollide(Object2D other) {
                if(!other.tags.contains("noCollision")) {
                    resolveCollision(other);
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
