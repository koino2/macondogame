package game.scripts.weapons.bulletScripts;

import game.scripts.weapons.BulletScript;
import lib.Object2D;

import java.util.ArrayList;
import java.util.List;

public class DestroyOnCollision extends BulletScript {
    public List<String> excludeTags = new ArrayList<>();

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
        if (!other.tags.contains("noCollision")){
            object.destroy();
        }
    }
}
