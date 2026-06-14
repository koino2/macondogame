package game.scripts.animations;

import lib.Script;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AnimatedTexture extends Script {
    public BufferedImage fullTexture;
    public int width;

    public double time = 0;
    public double totalTime;

    public BufferedImage[] frames;
    public int currentFrame;

    public AnimatedTexture(String path, double totalTime){
        this.totalTime = totalTime;
        try {
            fullTexture = ImageIO.read(new File(path));
            width = fullTexture.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        }

        frames = new BufferedImage[fullTexture.getHeight()/width];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = fullTexture.getSubimage(0, i*width, width, width);
        }
    }

    public void onAnimationEnd(){
        time = 0;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;
        double timePerFrame = (totalTime / frames.length);
        currentFrame = (int) Math.floor(time/timePerFrame);
        if(currentFrame < frames.length){
            object.texture = frames[currentFrame];
        } else{
            onAnimationEnd();
        }
    }
}
