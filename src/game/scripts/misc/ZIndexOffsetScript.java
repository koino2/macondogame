package game.scripts.misc;

import lib.Script;

public class ZIndexOffsetScript extends Script {
    public int offset = 0;

    public ZIndexOffsetScript(int offset){
        this.offset = offset;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        if (object.parent != null) {
            object.zIndex = object.parent.zIndex;
        } else {
            object.zIndex = offset;
        }
    }
}
