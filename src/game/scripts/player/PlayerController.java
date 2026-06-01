package game.scripts.player;

import game.scripts.weapons.Pistol;
import lib.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlayerController extends Script {
    Object2D player;

    Sound footstepsSound = new Sound("src/assets/footsteps.wav");

    Pistol pistol;

    public boolean shot = false;
    public float shotX = 0;
    public float shotY = 0;

    @Override
    public void start() {
        player = object;

        pistol = new Pistol(0, 0, "player");
        pistol.cooldown = 0.2f;
        pistol.offsetX = 60;
        pistol.offsetY = 5;
        pistol.bulletColor = new Color(255, 179, 50);

        object.sounds.add(footstepsSound);
        object.addScript(pistol);
    }

    @Override
    public void update(double deltaTime) {
        shot = false;

        Point point = Input.getMousePosition();
        float mouseWorldX = 0;
        float mouseWorldY = 0;
        if (point != null) {
            mouseWorldX = (float)((point.x-object.scene.engine.getWidth()/2.0)/object.scene.camera.scale+object.scene.camera.globalX);
            mouseWorldY = (float)((point.y-object.scene.engine.getHeight()/2.0)/object.scene.camera.scale+object.scene.camera.globalY);
            int xDist = (int) (mouseWorldX - player.globalX);
            int yDist = (int) (mouseWorldY - player.globalY);
            player.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));
        }

        if(Input.isMouseDown(MouseEvent.BUTTON1) && point != null){
            pistol.fire(new Point((int) mouseWorldX, (int) mouseWorldY));
            shot = true;
            shotX = mouseWorldX;
            shotY = mouseWorldY;
        }

        /*boolean shot = false;
        int shotX = 0;
        int shotY = 0;

        if (Input.isMouseDown(MouseEvent.BUTTON1) && point != null) {
            long timeNow = System.nanoTime();
            if(timeNow - lastShot > 0.2f * 1_000_000_000) {
                Sound sound = new Sound("src/assets/shoot.wav");
                sound.setVolume(0.5f);
                sound.play();
                float mouseWorldX = (float)((point.x-object.scene.engine.getWidth()/2.0)/object.scene.camera.scale+object.scene.camera.globalX);
                float mouseWorldY = (float)((point.y-object.scene.engine.getHeight()/2.0)/object.scene.camera.scale+object.scene.camera.globalY);
                Bullet bullet = new Bullet(new Point((int) mouseWorldX, (int) mouseWorldY), player, 60, 5, "player", 500);
                bullet.collisionScript.collidableObjects = object.scene.objects;
                object.scene.addObject(bullet);
                lastShot = timeNow;
                shot = true;
                shotX = (int) mouseWorldX;
                shotY = (int) mouseWorldY;
            }
        }*/

        player.xAcceleration = 0;
        player.yAcceleration = 0;

        float acceleration = 600;
        float damping = 1f;

        if (Input.isKeyDown(KeyEvent.VK_W) || Input.isKeyDown(KeyEvent.VK_S)) {

            if (Input.isKeyDown(KeyEvent.VK_W)) {
                player.yAcceleration = -acceleration;
            }
            if (Input.isKeyDown(KeyEvent.VK_S)) {
                player.yAcceleration = acceleration;
            }
        } else {
            player.yVelocity *= (float) Math.exp(-damping * deltaTime);
        }
        if (Input.isKeyDown(KeyEvent.VK_A) || Input.isKeyDown(KeyEvent.VK_D)) {
            if (Input.isKeyDown(KeyEvent.VK_A)) {
                player.xAcceleration = -acceleration;
            }
            if (Input.isKeyDown(KeyEvent.VK_D)) {
                player.xAcceleration = acceleration;
            }
        } else {
            player.xVelocity *= (float) Math.exp(-damping * deltaTime);
        }
        footstepsSound.clip.loop(-1);
        if(Input.isKeyDown(KeyEvent.VK_W) || Input.isKeyDown(KeyEvent.VK_S) || Input.isKeyDown(KeyEvent.VK_A) || Input.isKeyDown(KeyEvent.VK_D)){
            footstepsSound.resume();
        } else{
            footstepsSound.stop();
        }

        /*if (Input.isKeyDown(KeyEvent.VK_E)) {
            player.xSize += 1;
        }
        if (Input.isKeyDown(KeyEvent.VK_Q)) {
            player.xSize -= 1;
        }*/
    }

    @Override
    public void renderUI(Graphics g) {

    }
}