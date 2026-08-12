package game.prefabs.enemies;

import game.scripts.misc.DelayedAction;
import game.scripts.weapons.cannon.CannonExplosion;
import lib.*;

import java.awt.*;

public class Bomb extends Enemy{
    public String exclude = "enemy";
    public Bomb(int x, int y, int rotation) {
        super(x, y, rotation, 100, 100);

        texture = StaticTextures.read("src/assets/textures/entities/bomb.png");
        tags.add("noCollision");
    }

    boolean fired = false;

    public void explode(){

        if (fired) return;
        fired = true;

        Light glow = new Light(15, 15, 10);
        glow.color = new Color(250, 50, 50, 200);
        addChild(glow);

        Sound beep = new Sound("src/assets/audio/enemies/mine/beep.wav", 1);
        sounds.add(beep);
        beep.play();

        addScript(new DelayedAction(1){
            @Override
            public void action() {
                CannonExplosion explosion = new CannonExplosion(xPos, yPos);
                scene.addObject(explosion);
                destroy();
            }
        });
    }

    @Override
    public void onObjectStart() {
        scripts.remove(healthScript);
        scripts.remove(collisionScript);
        collisionScript = new CollisionScript() {
            @Override
            public void onCollide(Object2D other) {
                if(!other.tags.contains("noCollision") && !other.tags.contains(exclude)) {
                    explode();
                }
            }
        };
        addScript(collisionScript);
        collisionScript.collidableObjects = scene.objects;
    }
}
