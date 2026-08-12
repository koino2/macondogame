package game.prefabs.enemies;

import game.scripts.weapons.cannon.CannonExplosion;
import lib.CollisionScript;
import lib.Object2D;
import lib.Script;
import lib.StaticTextures;

import java.awt.*;

public class Bomb extends Enemy{
    public String exclude = "enemy";
    public Bomb(int x, int y, int rotation) {
        super(x, y, rotation, 100, 100, new Script() {
            @Override
            public void start() {

            }

            @Override
            public void update(double deltaTime) {

            }
        });

        texture = StaticTextures.read("src/assets/textures/entities/bomb.png");
        tags.add("noCollision");
    }

    public void explode(){
        CannonExplosion explosion = new CannonExplosion(xPos, yPos);
        scene.addObject(explosion);
        destroy();
    }

    @Override
    public void onObjectStart() {

        System.out.println("dihh");

        collisionScript = new CollisionScript() {
            @Override
            public void renderUI(Graphics g) {
                System.out.println("yee");
            }

            @Override
            public void onCollide(Object2D other) {
                System.out.println("touch");
                if(!other.tags.contains("noCollision") && !other.tags.contains(exclude)) {
                    explode();
                }
            }
        };
    }
}
