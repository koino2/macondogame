package game.prefabs;

import game.scripts.player.recording.Recording;
import game.scripts.player.recording.RecordingReader;
import game.scripts.weapons.WeaponScript;
import game.scripts.weapons.pistol.Pistol;
import lib.Object2D;
import lib.Script;
import lib.Sound;

import java.awt.*;

public class Ghost extends Object2D {

    public String spawnSoundPath = "src/assets/spawnGhost.wav";

    public void onRecordingFinished(){
        destroy();
    }

    public Ghost(Recording recording) {
        super(0, 0, 100, 100, 0);

        //System.out.println(recording.frames.size());

        tags.add("player");
        tags.add("ghost");

        WeaponScript weapon = recording.weapon;

        addScript(weapon);

        addScript(new Script() {
            @Override
            public void start() {
                Sound spawnSound = new Sound(spawnSoundPath, 1);
                sounds.add(spawnSound);
                spawnSound.setVolume(2);
                spawnSound.play();
            }
            @Override
            public void update(double deltaTime) {}
        });

        addScript(new RecordingReader(recording, weapon){
            @Override
            public void onRecordingFinished() {
                Ghost.this.onRecordingFinished();
            }
        });
    }

}
