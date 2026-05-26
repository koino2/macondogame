package lib;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {
    public Clip clip;
    public FloatControl pan;
    public FloatControl volume;

    public Sound(String path){
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path));
            clip = AudioSystem.getClip();
            clip.open(audio);
            pan = (FloatControl) clip.getControl(FloatControl.Type.PAN);
            volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(){
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
    public void pause(){
        clip.stop();
    }
    public void resume(){
        clip.start();
    }
    public void stop(){
        clip.stop();
        clip.setFramePosition(0);
    }
    public void setPan(float pan){
        this.pan.setValue(pan);
    }
    public void setVolume(float volume){
        this.volume.setValue(
                (float) (20f * Math.log10(Math.max(0.0001f, volume)))
        );
    }

}
