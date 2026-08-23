package game.prefabs.misc;

import lib.CollisionScript;
import lib.Object2D;

public abstract class PressurePlate extends Object2D {
    public boolean enabled = false;
    public abstract void onTrigger();
    public PressurePlate(float x, float y, float rotation) {
        super(x, y, 50, 50, rotation);

        addScript(new CollisionScript() {
            @Override
            public void onCollide(Object2D other) {
                if (other.tags.contains("player") && enabled){
                    onTrigger();
                }
            }
        });
    }
}
