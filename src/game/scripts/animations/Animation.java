package game.scripts.animations;

import lib.Script;

import java.util.ArrayList;
import java.util.List;

public class Animation extends Script {

    public List<AnimationKeyframe> frames = new ArrayList<>();

    public double time;
    public boolean playing = false;

    public void onAnimationEnd(){}

    public static float lerp(float a, float b, double t) {
        return (float) (a + (b - a) * t); // b-a is the stuff between the two values,
        // multiply that by how much of the keyframe is done,
        // and add that to a.
    }

    public void addKeyframe(double timeAdded, float x, float y, float xSize, float ySize, float rot){
        AnimationKeyframe frame = new AnimationKeyframe();
        frame.x = x;
        frame.y = y;
        frame.xSize = xSize;
        frame.ySize = ySize;
        frame.rotation = rot;

        if(!frames.isEmpty()){
            frame.time = frames.get(frames.size()-1).time + timeAdded;
        } else{
            frame.time = timeAdded;
        }

        frames.add(frame);
    }

    public void play(){
        time = 0;
        playing = true;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        if (playing) {

            time += deltaTime;

            AnimationKeyframe nextKeyFrame = null;
            AnimationKeyframe previousKeyFrame = null;
            for (int i = 0; i < frames.size(); i++) {
                if (frames.get(i).time <= time) {
                    previousKeyFrame = frames.get(i);
                }
                if (frames.get(i).time > time) {
                    nextKeyFrame = frames.get(i);
                    break;
                }
            }

            if(nextKeyFrame == null){
                playing = false;
                onAnimationEnd();
                return;
            }

            if (previousKeyFrame != null) {

                double t = (time - previousKeyFrame.time) / (nextKeyFrame.time - previousKeyFrame.time);

                object.xPos = lerp(previousKeyFrame.x, nextKeyFrame.x, t);
                object.yPos = lerp(previousKeyFrame.y, nextKeyFrame.y, t);
                object.xSize = lerp(previousKeyFrame.xSize, nextKeyFrame.xSize, t);
                object.ySize = lerp(previousKeyFrame.ySize, nextKeyFrame.ySize, t);
                object.rotation = lerp(previousKeyFrame.rotation, nextKeyFrame.rotation, t);

            }
        }
    }
}
