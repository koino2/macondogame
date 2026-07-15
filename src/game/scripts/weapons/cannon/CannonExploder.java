package game.scripts.weapons.cannon;

import game.levels.Level;
import game.scripts.weapons.BulletScript;
import lib.Object2D;

import java.util.ArrayList;
import java.util.List;

public class CannonExploder extends BulletScript {
    public List<String> excludeTags = new ArrayList<>();

    boolean exploded = false;

    @Override
    public void onBulletDestroy() {

    }

    @Override
    public void onBulletCollide(Object2D other) {
        for (int i = 0; i < excludeTags.size(); i++) {
            if (other.tags.contains(excludeTags.get(i))){
                return;
            }
        }

        if(exploded) return;
        exploded = true;
        CannonExplosion explosion = new CannonExplosion(bullet.xPos, bullet.yPos);
        explosion.excludeTags = excludeTags;
        ((Level)(bullet.scene)).trash.addChild(explosion);
        bullet.destroy();
    }
}
