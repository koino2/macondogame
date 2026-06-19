package game.prefabs;

import game.scripts.player.recording.Recording;
import game.scripts.player.recording.RecordingReader;
import game.scripts.weapons.Pistol;
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

        Pistol pistol = new Pistol();
        pistol = new Pistol(0, 0, 10, "player");
        pistol.cooldown = 0.2f;
        pistol.offsetX = 60;
        pistol.offsetY = 5;
        pistol.bulletColor = new Color(255, 179, 50);

        //System.out.println(recording.frames.size());

        tags.add("player");

        addScript(pistol);

        addScript(new Script() {
            @Override
            public void start() {
                Sound spawnSound = new Sound(spawnSoundPath);
                spawnSound.setVolume(2);
                spawnSound.play();
            }
            @Override
            public void update(double deltaTime) {}
        });

        addScript(new RecordingReader(recording, pistol){
            @Override
            public void onRecordingFinished() {
                Ghost.this.onRecordingFinished();
            }
        });
    }

}
