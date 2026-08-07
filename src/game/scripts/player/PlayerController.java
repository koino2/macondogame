package game.scripts.player;

import game.scripts.weapons.WeaponScript;
import game.scripts.weapons.cannon.Cannon;
import lib.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlayerController extends Script {
    Object2D player;

    Sound footstepsSound;

    public float acceleration = 600;

    @Override
    public void start() {
        player = object;

        footstepsSound = new Sound("src/assets/footsteps.wav", 1);
        object.sounds.add(footstepsSound);
        //object.addScript(weapon);
    }

    @Override
    public void update(double deltaTime) {

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

        player.xAcceleration = 0;
        player.yAcceleration = 0;

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
        if(Input.isKeyDown(KeyEvent.VK_W) || Input.isKeyDown(KeyEvent.VK_S) || Input.isKeyDown(KeyEvent.VK_A) || Input.isKeyDown(KeyEvent.VK_D)){
            footstepsSound.resume();
            if(footstepsSound.clip.getFramePosition() > footstepsSound.clip.getFrameLength() - 50){
                footstepsSound.clip.setFramePosition(0);
            }
        } else{
            footstepsSound.pause();
        }
    }

    @Override
    public void renderUI(Graphics g) {

    }
}