package game.scripts.player.recording;

import game.prefabs.Player;
import lib.Script;

public class PlayerRecorder extends Script {
    public Player player;

    public Recording recording = new Recording();

    public PlayerRecorder(Player player){
        this.player = player;
    }

    double time;

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;

        RecordingFrame frame = new RecordingFrame();
        frame.x = player.globalX;
        frame.y = player.globalY;
        frame.rotation = player.globalRotation;
        frame.shot = player.playerControllerScript.shot;
        frame.shotX = player.playerControllerScript.shotX;
        frame.shotY = player.playerControllerScript.shotY;
        frame.time = time;

        recording.addFrame(frame);
        recording.weapon = player.playerControllerScript.weapon;
    }

    public void stopRecording(){
        time = 0;
        recording = new Recording();
    }
}
