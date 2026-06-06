package game.scripts.player.recording;

import game.scripts.weapons.WeaponScript;
import lib.Script;

import java.awt.*;

public class RecordingReader extends Script {
    public Recording recording;
    public WeaponScript weapon;

    public RecordingReader(Recording recording){
        this.recording = recording;
    }

    public RecordingReader(Recording recording, WeaponScript weapon){
        this.recording = recording;
        this.weapon = weapon;
    }

    public void onRecordingFinished(){
        object.destroy();
    };

    double time;

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;

        RecordingFrame frame = recording.getFrame(time);

        if(frame != null && Math.abs(frame.time - time) < 0.1f){
            object.xPos = frame.x;
            object.yPos = frame.y;
            object.rotation = frame.rotation;

            if(frame.shot){
                weapon.fire(new Point((int) frame.shotX, (int) frame.shotY));
            }
        }

        if(time > recording.frames.get(recording.frames.size()-1).time){
            onRecordingFinished();
        }
    }
}
