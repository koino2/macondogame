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

        if (Input.isMousePressed(MouseEvent.BUTTON1) && point != null) {
            float distanceX = point.x - player.xPos;
            float distanceY = point.y - player.yPos;

            float hypotenuse = (float) Math.sqrt(distanceX*distanceX + distanceY*distanceY);

            distanceX /= hypotenuse;
            distanceY /= hypotenuse;

            float rotation = (float) Math.toDegrees(Math.atan2(distanceY, distanceX));

            float offsetX = 50;
            float offsetY = 5;

            float rad = (float) Math.toRadians(player.rotation);

            float rotatedX = (float)(offsetX * Math.cos(rad) - offsetY * Math.sin(rad));
            float rotatedY = (float)(offsetX * Math.sin(rad) + offsetY * Math.cos(rad)); // gpt did that i have no idea what cos, sin is

            Object2D bullet = new Object2D(player.xPos+rotatedX, player.yPos+rotatedY, 10,10, rotation);
            bullet.color = new Color(255, 203, 26);

            bullet.xVelocity = distanceX * 500;
            bullet.yVelocity = distanceY * 500;

            objects.add(bullet);
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

        if(Input.isKeyDown(KeyEvent.VK_E)){
            player.xSize += 1;
        }
        if(Input.isKeyDown(KeyEvent.VK_Q)){
            player.xSize -= 1;
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
