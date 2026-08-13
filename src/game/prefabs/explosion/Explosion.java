package game.prefabs.explosion;

import game.scripts.misc.LifetimeScript;
import game.scripts.weapons.cannon.CannonExplosionAnimation;
import game.scripts.weapons.flamethrower.FlamethrowerParticleEmitter;
import lib.*;

import java.util.List;

public class Explosion extends Object2D {

    public List<String> excludeTags;
    public Explosion(float x, float y){
        super(x, y, 0, 0, 0);
        this.texture = StaticTextures.circle(400);

        tags.add("noCollision");

        Sound sound = new Sound("src/assets/cannon-explode.wav", 1);
        sound.setVolume(3);
        sounds.add(sound);
        sound.play();

        addScript(new Script() {
            @Override
            public void start() {
                ExplosionCollisionScript collisionScript = new ExplosionCollisionScript();
                collisionScript.collidableObjects = scene.objects;
                collisionScript.excludeTags = excludeTags;
                addScript(collisionScript);
            }

            @Override
            public void update(double deltaTime) {

            }
        });

        ExplosionAnimation anim = new ExplosionAnimation();
        anim.radius = 500;
        addScript(anim);
    }
}
