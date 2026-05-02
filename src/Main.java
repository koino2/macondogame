import lib.Object2D;
import lib.Scene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Untitled Macondo Game");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setSize(1200, 700);
        window.setLocationRelativeTo(null);

        window.setVisible(true);

        Scene scene = new Scene() {
            @Override
            public void update(double dt) {
                objects.get(0).xSize+=1;
            }

            @Override
            public void start() {
                Object2D player = new Object2D(200, 200, 50, 50, 0);
                try {
                    player.texture = ImageIO.read(new File("src/assets/player.png"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                this.objects.add(player);
            }
        };

        window.setContentPane(scene);
        window.repaint();
    }
}