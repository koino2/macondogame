package game.scripts.misc;

import lib.Object2D;
import lib.Script;
import lib.Sound;

public class Settings extends Script {

    public float volume = 0.5f;

    public void setVolume(float newVolume){
        volume = newVolume;
        for (int i = 0; i < object.scene.objects.size(); i++) {
            for (int j = 0; j < object.scene.objects.get(i).getDescendants().size(); j++) {
                for (Sound sound : object.scene.objects.get(i).getDescendants().get(j).sounds){
                    sound.volumeMultiplier = volume;
                }
            }
            for (Sound sound : object.scene.objects.get(i).sounds){
                sound.volumeMultiplier = volume;
            }
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {

    }
}
