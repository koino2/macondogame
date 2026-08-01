package game.scripts.player.weaponrecording;

import java.awt.*;

public class WeaponRecordingFrame {

    public double time;

    public boolean shot = false;
    public Point target;

    public WeaponRecordingFrame(double time, boolean shot, Point target){
        this.time = time;
        this.shot = shot;
        this.target = target;
    }
}
