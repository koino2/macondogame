package game.scripts.misc;

import lib.Script;

public class DelayedAction extends Script {

    public double time;
    public double delay;
    public boolean fired = false;

    public DelayedAction(double delay){
        this.delay = delay;
    }

    public void action(){
        if (fired) return;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        time += delay;
        if (time >= delay){
            action();
            fired = true;
        }
    }
}
