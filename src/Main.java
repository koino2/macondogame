import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main{
    public static void main(String[] args){
        JFrame window = new JFrame("Untitled Macondo Game");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setSize(1200,700);
        window.setLocationRelativeTo(null);

        MainPanel panel = new MainPanel();
        panel.setSize(window.getSize());
        panel.setLocation(0,0);
        window.add(panel);
        panel.setVisible(true);

        window.setVisible(true);
    }

    public static class MainPanel extends JPanel{
        Object2D player;
        public MainPanel(){
            player = new Object2D(
                    100,100,200,200, 0
            );
            try {
                player.texture = ImageIO.read(new File("src/assets/player.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            player.color = new Color(255, 255, 255);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Point point = getMousePosition();
            if (point != null) {
                int xDist = point.x - player.xPos;
                int yDist = point.y - player.yPos;
                player.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));
            }

            player.render((Graphics2D)g);
            //object.rotation += 1f;
            repaint();
        }
    }
}