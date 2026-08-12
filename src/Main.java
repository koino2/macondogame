import game.levels.Level1;
import game.levels.Level2;
import lib.Engine;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Untitled Macondo Game");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1200, 700);
        window.setLocationRelativeTo(null);

        Level2 scene = new Level2();
        Engine engine = new Engine(scene);
        window.setContentPane(engine);
        window.setVisible(true);

        // RANDOM COMMENT TO SEE IF MY VCS FIXED
        // it fixed :D
    }
}