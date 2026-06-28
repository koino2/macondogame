package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class Input {
    private static final Set<Integer> keys = new HashSet<>();
    private static Point mousePos = new Point(100,100);

    private static final Set<Integer> mouseButtons = new HashSet<>();
    private static final Set<Integer> mousePressed = new HashSet<>();
    private static final Set<Integer> mouseReleased = new HashSet<>();

    private static final Set<Integer> keysPressed = new HashSet<>();
    private static final Set<Integer> keysReleased = new HashSet<>();

    public static float scrollDelta = 0;

    public static void attach(JPanel panel){
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!keys.contains(e.getKeyCode())){
                    keysPressed.add(e.getKeyCode());
                }
                keys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys.remove(e.getKeyCode());
                keysReleased.add(e.getKeyCode());
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
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseButtons.add(e.getButton());
                mousePressed.add(e.getButton());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseButtons.remove(e.getButton());
                mouseReleased.add(e.getButton());
            }
        });

        panel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                scrollDelta = e.getWheelRotation();
            }
        });

        panel.setFocusable(true);
        panel.requestFocus();
    }
    public static void endFrame(){
        keysPressed.clear();
        keysReleased.clear();
        mousePressed.clear();
        mouseReleased.clear();
        scrollDelta = 0;
    }

    public static boolean isKeyDown(int key){
        return keys.contains(key);
    }
    public static boolean isKeyPressed(int key){
        return keysPressed.contains(key);
    }
    public static boolean isKeyReleased(int key){
        return keysReleased.contains(key);
    }
    public static Point getMousePosition(){
        return mousePos;
    }
    public static boolean isMouseDown(int button){
        return mouseButtons.contains(button);
    }
    public static boolean isMousePressed(int button){
        return mousePressed.contains(button);
    }
    public static boolean isMouseReleased(int button){
        return mouseReleased.contains(button);
    }
}
