package lib;

import game.scripts.misc.Settings;

import javax.sound.sampled.*;
import java.io.File;

public class Sound{
    public Clip clip;
    public FloatControl pan;
    public FloatControl volume;

    public float defaultVolume;
    public float volumeMultiplier;

    String path;

    public Sound(String path, float defaultVolume, float defaultVolumeMult){
        this.defaultVolume = defaultVolume;
        this.volumeMultiplier = defaultVolumeMult;
        setVolume(defaultVolume);
        this.path = path;
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

    public Sound(String path, float defaultVolume){
        this.defaultVolume = defaultVolume;
        this.volumeMultiplier = Settings.volume;
        setVolume(defaultVolume);
        this.path = path;
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

    public void setVolume(float volume) {
        if (this.volume == null) return;
        this.defaultVolume = volume;
        float db = (float) (20f * Math.log10(Math.max(0.0001f, volume * volumeMultiplier)));

        db = Math.max(this.volume.getMinimum(),
                Math.min(this.volume.getMaximum(), db));

        this.volume.setValue(db);
    }

}
