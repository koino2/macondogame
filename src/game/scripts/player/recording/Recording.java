package game.scripts.player.recording;

import game.scripts.weapons.WeaponScript;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Recording{
    public List<RecordingFrame> frames = new ArrayList<>();

    public WeaponScript weapon;

    public void addFrame(RecordingFrame frame){
        frames.add(frame);
    }

    public RecordingFrame getFrame(double time){
        double bestDifference = Float.POSITIVE_INFINITY;
        RecordingFrame closestFrame = null;
        for (int i = 0; i < frames.size(); i++) {
            RecordingFrame frame = frames.get(i);
            double difference = Math.abs(frame.time - time);
            if(difference < bestDifference){
                closestFrame = frame;
                bestDifference = difference;
            }
        }
        return closestFrame;
    }
}
