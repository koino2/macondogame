package scripts;

import lib.Input;
import lib.Object2D;
import lib.Script;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlayerController extends Script {
    Object2D player;

    @Override
    public void start() {
        player = object;
    }

    @Override
    public void update(double deltaTime) {
        Point point = Input.getMousePosition();
        if (point != null) {
            int xDist = (int) (point.x - player.xPos);
            int yDist = (int) (point.y - player.yPos);
            player.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));
        }

        if (Input.isMousePressed(MouseEvent.BUTTON1) && point != null) {
            object.scene.addObject(new Bullet(player.xPos, player.yPos, point, player, 50, 5));
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

        if (Input.isKeyDown(KeyEvent.VK_E)) {
            player.xSize += 1;
        }
        if (Input.isKeyDown(KeyEvent.VK_Q)) {
            player.xSize -= 1;
        }
    }

    @Override
    public void renderUI(Graphics g) {

    }
}