package game.scripts.weapons;

import lib.Object2D;
import lib.Script;
import lib.Sound;

import java.awt.*;

public abstract class WeaponScript extends Script {

    public String soundPath = "src/assets/shoot.wav";
    public float soundVolume = 0.5f;

    public float cooldown = 0.1f;

    public double timer = 0;


    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        timer += deltaTime;
    }

    public boolean canFire(){
        return timer >= cooldown;
    }
    public void resetTimer(){
        timer = 0;
    }
    public void playSound(){
        Sound sound = new Sound(soundPath);
        sound.setVolume(soundVolume);
        sound.play();
    }

    public abstract void fireAtObject(Object2D target);
    public abstract void fire(Point target);
}
