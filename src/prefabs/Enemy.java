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

        enemyScript = new EnemyScript() {
            double attackTimer = 0;
            float attackInterval = 1f;
            float attackRadius = 200;
            float attackDamage = 20;
            @Override
            public void behaviour(double deltaTime) {
                attackTimer += deltaTime;

                if (attackTimer >= attackInterval) {
                    for (int i = 0; i < object.scene.objects.size(); i++) {
                        Object2D object2D = object.scene.objects.get(i);
                        if(object2D.tags.contains("player")) {
                            if (Math.hypot(object2D.xPos - object.xPos, object2D.yPos - object.yPos) < attackRadius) {
                                ((Player)(object2D)).healthScript.damage(attackDamage);
                                attackTimer = 0;
                            }
                        }
                    }
                }
            }
        };
        addScript(enemyScript);
    }
}
