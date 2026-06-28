package game.scripts.weapons.cannon;

import lib.*;

import java.util.List;

public class CannonExplosion extends Object2D {

    public List<String> excludeTags;
    public CannonExplosion(float x, float y){
        super(x, y, 0, 0, 0);
        this.texture = StaticTextures.circle(400);

        tags.add("noCollision");

        Sound sound = new Sound("src/assets/cannon-explode.wav");
        sound.setVolume(3);
        sounds.add(sound);
        sound.play();

        addScript(new Script() {
            @Override
            public void start() {
                CannonExplosionCollisionScript collisionScript = new CannonExplosionCollisionScript();
                collisionScript.collidableObjects = scene.objects;
                collisionScript.excludeTags = excludeTags;
                addScript(collisionScript);
            }

            @Override
            public void update(double deltaTime) {

            }
        });

        CannonExplosionAnimation anim = new CannonExplosionAnimation();
        addScript(anim);
    }
}
