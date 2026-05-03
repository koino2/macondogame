import lib.Input;
import lib.Object2D;
import lib.Scene;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class SampleScene extends Scene {
    Object2D player;
    @Override
    public void update(double deltaTime) {
        Point point = Input.getMousePosition();
        if (point != null) {
            int xDist = (int) (point.x - player.xPos);
            int yDist = (int) (point.y - player.yPos);
            player.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));
        }

        if (Input.isMousePressed(MouseEvent.BUTTON1)){
            System.out.println("pew!");
        }

        player.xAcceleration = 0;
        player.yAcceleration = 0;

        float acceleration = 600;
        float damping = 1f;

        if(Input.isKeyDown(KeyEvent.VK_W) || Input.isKeyDown(KeyEvent.VK_S)) {

            if (Input.isKeyDown(KeyEvent.VK_W)) {
                player.yAcceleration = -acceleration;
            }
            if (Input.isKeyDown(KeyEvent.VK_S)) {
                player.yAcceleration = acceleration;
            }
        } else{
            player.yVelocity *= (float) Math.exp(-damping * deltaTime);
            // no idea how you can exponentiate a decimal but ok chatgpt it works
        }
        if(Input.isKeyDown(KeyEvent.VK_A) || Input.isKeyDown(KeyEvent.VK_D)) {
            if (Input.isKeyDown(KeyEvent.VK_A)) {
                player.xAcceleration = -acceleration;
            }
            if (Input.isKeyDown(KeyEvent.VK_D)) {
                player.xAcceleration = acceleration;
            }
        } else{
            player.xVelocity *= (float) Math.exp(-damping * deltaTime);
        }
    }

    @Override
    public void start() {
        player = new Object2D(200,200,100,100,0);
        try {
            player.texture = ImageIO.read(new File("src/assets/player.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        objects.add(player);
    }
}
