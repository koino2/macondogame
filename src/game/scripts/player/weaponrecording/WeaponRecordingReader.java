package game.scripts.player.weaponrecording;

import game.scripts.weapons.WeaponScript;
import lib.Script;

import java.awt.*;

public class WeaponRecordingReader extends Script {
    public WeaponRecording recording;
    public WeaponScript weapon;

    public WeaponRecordingReader(WeaponRecording recording){
        this.recording = recording;
        try {
            weapon = recording.weapon.getClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WeaponRecordingReader(WeaponRecording recording, WeaponScript weapon){
        this.recording = recording;
        this.weapon = weapon;
    }

    public void onRecordingFinished(){
        object.destroy();
    };

    double time;

    @Override
    public void start() {
        object.addScript(weapon);
    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;

        WeaponRecordingFrame frame = recording.getFrame(time);

        if(frame != null && Math.abs(frame.time - time) < 0.1f){
            if(frame.shot){
                weapon.fire(new Point(frame.target.x, frame.target.y));
            }
        }

        if(time > recording.frames.get(recording.frames.size()-1).time){
            onRecordingFinished();
        }
    }
}