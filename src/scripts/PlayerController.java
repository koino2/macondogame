package scripts;

import lib.*;
import prefabs.Bullet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayerController extends Script {
    Object2D player;

    Sound footstepsSound = new Sound("src/assets/footsteps.wav");

    public List<PlayerRecording> recording = new ArrayList<>();
    public double time;

    @Override
    public void start() {
        player = object;
    }

    long lastShot;
    @Override
    public void update(double deltaTime) {
        Point point = Input.getMousePosition();
        if (point != null) {
            float mouseWorldX = (float)((point.x-object.scene.engine.getWidth()/2.0)/object.scene.camera.scale+object.scene.camera.globalX);
            float mouseWorldY = (float)((point.y-object.scene.engine.getHeight()/2.0)/object.scene.camera.scale+object.scene.camera.globalY);
            int xDist = (int) (mouseWorldX - player.globalX);
            int yDist = (int) (mouseWorldY - player.globalY);
            player.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));
        }

        boolean shot = false;
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
        }

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

        time += deltaTime;
        PlayerRecording tickRecording = new PlayerRecording();
        tickRecording.time = time;
        tickRecording.x = player.xPos;
        tickRecording.y = player.yPos;
        tickRecording.shot = shot;
        tickRecording.shotX = shotX;
        tickRecording.shotY = shotY;
        recording.add(tickRecording);
    }

    @Override
    public void renderUI(Graphics g) {

    }
}