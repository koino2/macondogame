package game.scripts.npc;

import lib.Object2D;
import lib.Script;

public class ClosestTargetScript extends Script {

    public MoveScript moveScript = new MoveScript();

    public Object2D closest = null;

    public String targetTag;

    public ClosestTargetScript(String targetTag){
        this.targetTag = targetTag;
    }

    @Override
    public void start() {
        object.addScript(moveScript);
    }

    @Override
    public void update(double deltaTime) {
        closest = null;
        float closestHypot = 0;
        for(int i = 0; i < object.scene.objects.size(); i++){
            float distX = object.scene.objects.get(i).xPos - object.xPos;
            float distY = object.scene.objects.get(i).yPos - object.yPos;
            float hypot = (float) Math.hypot(distX, distY);
            if(object.scene.objects.get(i) != object && object.scene.objects.get(i).tags.contains("player")) {
                if(closest == null || hypot < closestHypot) {
                    closestHypot = hypot;
                    closest = object.scene.objects.get(i);

                    moveScript.target = closest;
                }
            }
        }
    }

}
