package game.prefabs.enemies;

import game.prefabs.explosion.Explosion;
import game.scripts.misc.DelayedAction;
import game.scripts.misc.LifetimeScript;
import game.scripts.misc.Settings;
import game.scripts.weapons.flamethrower.FlamethrowerParticleEmitter;
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

        Sound beep = new Sound("src/assets/audio/enemies/mine/beep.wav", 1, Settings.volume);
        beep.setVolume(beep.defaultVolume);
        sounds.add(beep);
        beep.play();

        addScript(new DelayedAction(1){
            @Override
            public void action() {
                Explosion explosion = new Explosion(globalX, globalY);
                scene.addObject(explosion);

                FlamethrowerParticleEmitter emitter = new FlamethrowerParticleEmitter(){
                    @Override
                    public void onObjectStart() {
                        addScript(new LifetimeScript(0.25f));
                    }
                };
                emitter.xPos = object.globalX;
                emitter.yPos = object.globalY;
                emitter.spread = 360;
                emitter.spawnTime = 0.05f;
                emitter.particlesPerSpawn = 10;
                object.scene.addObject(emitter);

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
                if (other.tags.contains("bullet")){
                    explode();
                }
            }
        };
        addScript(collisionScript);
        collisionScript.collidableObjects = scene.objects;
    }
}
