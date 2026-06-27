package game.scripts.weapons.cannon;

import game.scripts.animations.Animation;
import lib.Script;

import java.awt.*;

public class CannonExplosionAnimation extends Script {
    @Override
    public void start() {
        Animation begin = new Animation(){
            @Override
            public void onAnimationEnd(){
                Animation end = new Animation(){
                    public void start(){

                    }
                    @Override
                    public void onAnimationEnd() {
                        object.destroy();
                    }
                };
                end.addKeyframe(0, object.xPos, object.yPos, object.xSize, object.ySize, 0, object.color);
                end.addKeyframe(2, object.xPos, object.yPos, 0, 0, 0, new Color(237, 183, 121, 129));
                object.addScript(end);
                end.play();
            }
        };
        begin.addKeyframe(0, object.xPos, object.yPos, 20, 20, 0, new Color(122, 56, 47, 56));
        begin.addKeyframe(.5f, object.xPos, object.yPos, 200, 200, 360, new Color(216, 109, 44, 134));
        object.addScript(begin);
        begin.play();
    }

    @Override
    public void update(double deltaTime) {

    }
}
