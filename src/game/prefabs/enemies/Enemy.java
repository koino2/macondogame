package game.prefabs.enemies;

import lib.*;
import lib.CollisionScript;
import game.scripts.misc.HealthScript;

public class Enemy extends Object2D {

    public CollisionScript collisionScript;
    public HealthScript healthScript;

    public Enemy(int x, int y, int rotation, int width, int height){
        super(x, y, width, height, rotation);

        tags.add("enemy");

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

        /*collisionScript.collidableTags.add("bullet");
        collisionScript.collidableTags.add("wall");*/
        addScript(collisionScript);
    }

    public Enemy(int x, int y, int rotation, int width, int height, CollisionScript collisionScript){
        super(x, y, width, height, rotation);

        tags.add("enemy");

        healthScript = new HealthScript() {
            @Override
            public void onDamage(float damageAmount) {
                if (health <= 0) {
                    destroy();
                }
            }
        };
        addScript(healthScript);

        this.collisionScript = collisionScript;
        addScript(this.collisionScript);
    }
}
