package scripts;

import lib.Object2D;
import lib.Script;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnemyScript extends Script {
    List<String> targetTags = new ArrayList<>();
    public EnemyScript() {

    }

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {
        Object2D closest = null;
        float closestHypot = 0;
        for(int i = 0; i < object.scene.objects.size(); i++){
            float distX = Math.abs(object.scene.objects.get(i).xPos - object.xPos);
            float distY = Math.abs(object.scene.objects.get(i).yPos - object.yPos);
            float hypot = (float) Math.hypot(distX, distY);
            if(object.scene.objects.get(i) != object) {
                if(closest == null || hypot < closestHypot) {
                    closestHypot = hypot;
                    closest = object.scene.objects.get(i);
                }
            }
        }
    }

    @Override
    public void renderUI(Graphics g) {

    }
}
