import game.levels.Level1;
import game.levels.Level2;
import game.levels.MainLevel;
import lib.Engine;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Untitled Macondo Game");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1200, 700);
        window.setLocationRelativeTo(null);

        MainLevel scene = new MainLevel();
        Engine engine = new Engine(scene);
        window.setContentPane(engine);
        window.setVisible(true);

        // RANDOM COMMENT TO SEE IF MY VCS FIXED
        // it fixed :D
    }
}