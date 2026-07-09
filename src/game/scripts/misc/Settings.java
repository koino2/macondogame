package game.scripts.misc;

import lib.Object2D;
import lib.Script;
import lib.Sound;

import java.util.List;

public class Settings extends Script {

    public static float volume = 0.1f;

    public void setVolume(float newVolume){
        volume = newVolume;

        for (Object2D obj : object.scene.objects){
            for (Object2D descendant : obj.getDescendants()){
                for (Sound sound : descendant.sounds){
                    sound.volumeMultiplier = volume;
                    sound.setVolume(sound.defaultVolume);
                }
            }
            for (Sound sound : obj.sounds){
                sound.volumeMultiplier = volume;
                sound.setVolume(sound.defaultVolume);
            }
        }
    }

    public void updateSettings(){
        setVolume(volume);
        System.out.println("update");
    }

    @Override
    public void start() {
        updateSettings();
    }

    @Override
    public void update(double deltaTime) {
        //setVolume(volume);
    }
}
