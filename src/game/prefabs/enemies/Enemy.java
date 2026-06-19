package game.prefabs.enemies;

import lib.*;
import lib.CollisionScript;
import game.scripts.enemies.EnemyScript;
import game.scripts.misc.HealthScript;

import java.awt.*;

public class Enemy extends Object2D {

    public CollisionScript collisionScript;
    public Script enemyScript;
    public HealthScript healthScript;

    public Enemy(int x, int y, int rotation, int width, int height, Script enemyScript){
        super(x, y, width, height, rotation);

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

        this.enemyScript = enemyScript;
        addScript(this.enemyScript);
    }
}
