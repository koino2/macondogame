package game.prefabs.misc;

import lib.CollisionScript;
import lib.Object2D;

public abstract class PressurePlate extends Object2D {
    public boolean enabled = false;
    public abstract void onTrigger();
    CollisionScript collisionScript;
    public PressurePlate(float x, float y, float rotation) {
        super(x, y, 50, 50, rotation);

        collisionScript = new  CollisionScript() {
            @Override
            public void onCollide(Object2D other) {
                if (!other.tags.contains("noCollision") && enabled){
                    onTrigger();
                }
            }
        };
        addScript(collisionScript);
    }

    @Override
    public void onObjectStart() {
        collisionScript.collidableObjects = scene.objects;
    }
}
