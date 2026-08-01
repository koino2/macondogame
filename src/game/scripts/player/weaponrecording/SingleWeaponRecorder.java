package game.scripts.player.weaponrecording;

import game.scripts.weapons.WeaponScript;
import lib.Script;

public class SingleWeaponRecorder extends Script {
    public WeaponScript weapon;

    public WeaponRecording recording = new WeaponRecording();

    public SingleWeaponRecorder(WeaponScript weapon){
        this.weapon = weapon;
    }

    double time;

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;

        WeaponRecordingFrame frame = new WeaponRecordingFrame(
                time,
                weapon.shot,
                weapon.target
        );

        recording.addFrame(frame);
        recording.weapon = weapon;
    }

    public void stopRecording(){
        time = 0;
        recording = new WeaponRecording();
    }
}