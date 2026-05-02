import lib.Engine;
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

        SampleScene sampleScene = new SampleScene();
        Engine engine = new Engine(sampleScene);
        window.add(engine);
    }
}