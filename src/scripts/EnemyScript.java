package scripts;

import lib.Object2D;
import lib.Script;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class EnemyScript extends Script {
    List<String> targetTags = new ArrayList<>();

    @Override
    public void start() {

    }

    public abstract void behaviour(double deltaTime);
    @Override
    public void update(double deltaTime) {
        behaviour(deltaTime);
    }

    @Override
    public void renderUI(Graphics g) {

    }
}
