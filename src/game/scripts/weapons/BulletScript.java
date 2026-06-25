package game.scripts.weapons;

import game.prefabs.Bullet;
import lib.Object2D;
import lib.Script;

public abstract class BulletScript extends Script {
    public Bullet bullet;

    public abstract void onBulletDestroy();
    public abstract void onBulletCollide(Object2D other);

    @Override
    public void start() {
        bullet = (Bullet) object;
    }

    @Override
    public void update(double deltaTime) {

    }
}
