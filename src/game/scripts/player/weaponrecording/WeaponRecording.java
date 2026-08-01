package game.scripts.player.weaponrecording;

import game.scripts.weapons.WeaponScript;

import java.util.ArrayList;
import java.util.List;

public class WeaponRecording{
    public List<WeaponRecordingFrame> frames = new ArrayList<>();

    public WeaponScript weapon;

    public void addFrame(WeaponRecordingFrame frame){
        frames.add(frame);
    }

    public WeaponRecordingFrame getFrame(double time){
        double bestDifference = Float.POSITIVE_INFINITY;
        WeaponRecordingFrame closestFrame = null;
        for (int i = 0; i < frames.size(); i++) {
            WeaponRecordingFrame frame = frames.get(i);
            double difference = Math.abs(frame.time - time);
            if(difference < bestDifference){
                closestFrame = frame;
                bestDifference = difference;
            }
        }
        return closestFrame;
    }
}
