package game.scripts.misc;

import lib.Script;

public class LifetimeScript extends Script {

    public double lifetime = 5;
    public double time = 0;

    public LifetimeScript(double lifetime){
        this.lifetime = lifetime;
    }

    public void onLifetimeEnd(){
        object.destroy();
    }

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;
        if (time >= lifetime){
            onLifetimeEnd();
        }
    }
}
