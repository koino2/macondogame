package game.scripts.weapons.cannon;

import game.scripts.misc.HealthScript;
import lib.CollisionScript;
import lib.Object2D;

import java.util.ArrayList;
import java.util.List;

public class CannonExplosionCollisionScript extends CollisionScript {
    public float damage = 50;

    public List<Object2D> damagedObjects = new ArrayList<>();

    public List<String> excludeTags = new ArrayList<>();
    @Override
    public void onCollide(Object2D other) {
        for (int i = 0; i < excludeTags.size(); i++) {
            if (other.tags.contains(excludeTags.get(i))) return;
        }

        if (damagedObjects.contains(other)) return;
        float distX = other.globalX - object.globalX;
        float distY = other.globalY - object.globalY;

        float radius = object.xSize/2;

        if((distX*distX+distY*distY) <= Math.pow(radius+(other.xSize/2), 2)){
            for (int i = 0; i < other.scripts.size(); i++) {
                if(other.scripts.get(i) instanceof HealthScript healthScript){
                    healthScript.damage(damage);
                    damagedObjects.add(other);
                }
            }
        }
    }
}
