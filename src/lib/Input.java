package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class Input {
    private static final Set<Integer> keys = new HashSet<>();
    private static Point mousePos = new Point(100,100);

    public static void attach(JPanel panel){
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys.remove(e.getKeyCode());
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mousePos = e.getPoint();
            }
        });

        panel.setFocusable(true);
        panel.requestFocus();
    }
    public static boolean isKeyDown(int key){
        return keys.contains(key);
    }
    public static Point getMousePosition(){
        return mousePos;
    }
}
