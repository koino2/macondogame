package game.scripts.weapons;

import game.prefabs.Player;
import lib.Input;
import lib.Object2D;
import lib.Script;
import lib.Sound;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class WeaponScript extends Script {

    public String soundPath = "src/assets/shoot.wav";
    public float soundVolume = 0.5f;

    public float cooldown = 0.1f;

    public double timer = 0;


    @Override
    public void start() {

    }

    public Point getMouseWorldPosition(){
        Point point = Input.getMousePosition();
        float mouseWorldX = 0;
        float mouseWorldY = 0;
        if (point != null) {
            mouseWorldX = (float)((point.x-object.scene.engine.getWidth()/2.0)/object.scene.camera.scale+object.scene.camera.globalX);
            mouseWorldY = (float)((point.y-object.scene.engine.getHeight()/2.0)/object.scene.camera.scale+object.scene.camera.globalY);
            return new Point((int) mouseWorldX, (int) mouseWorldY);
        }
        return null;
    }

    public void behaviour(double deltaTime){
        if (!live) return;

        if(Input.isMouseDown(MouseEvent.BUTTON1)){
            Point mouseWorldPosition = getMouseWorldPosition();
            if (mouseWorldPosition == null) return;
            fire(mouseWorldPosition);
            shot = true;
            target = mouseWorldPosition;
        }
    }

    public boolean live;

    public boolean shot = false;
    public Point target;

    @Override
    public void update(double deltaTime) {
        timer += deltaTime;

        shot = false;

        live = object instanceof Player;

        behaviour(deltaTime);
    }

    public boolean canFire(){
        return timer >= cooldown;
    }
    public void resetTimer(){
        timer = 0;
    }
    public void playSound(){
        Sound sound = new Sound(soundPath, 1);
        object.sounds.add(sound);
        sound.setVolume(soundVolume);
        sound.play();
    }

    public abstract void fire(Point target);
}
