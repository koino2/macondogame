package game.scripts.weapons;

import lib.Object2D;
import lib.Script;
import lib.Sound;

public abstract class WeaponScript extends Script {

    String soundPath = "src/assets/shoot.wav";

    public float cooldown = 0.5f;

    public double timer = 0;


    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        timer += deltaTime;
    }

    public boolean canFire(){
        return timer >= cooldown;
    }
    public void resetTimer(){
        timer = 0;
    }

    public void fire(Object2D target){
        if(canFire()){
            new Sound(soundPath).play();

            // ADD CODE HERE

            resetTimer();
        }
    }
}
