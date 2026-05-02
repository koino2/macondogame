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

        if(Input.isKeyDown(KeyEvent.VK_W)){player.yPos-=200 * deltaTime;}
        if(Input.isKeyDown(KeyEvent.VK_S)){player.yPos+=200 * deltaTime;}
        if(Input.isKeyDown(KeyEvent.VK_A)){player.xPos-=200 * deltaTime;}
        if(Input.isKeyDown(KeyEvent.VK_D)){player.xPos+=200 * deltaTime;}
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
