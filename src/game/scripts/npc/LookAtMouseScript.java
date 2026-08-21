package game.scripts.npc;

import lib.Input;

import java.awt.*;

public class LookAtMouseScript extends LookAtScript {

    @Override
    public void behaviour(double deltaTime) {
        Point point = Input.getMousePosition();
        float mouseWorldX;
        float mouseWorldY;

        if (point != null) {
            mouseWorldX = (float)((point.x-object.scene.engine.getWidth()/2.0)/object.scene.camera.scale+object.scene.camera.globalX);
            mouseWorldY = (float)((point.y-object.scene.engine.getHeight()/2.0)/object.scene.camera.scale+object.scene.camera.globalY);
            
            target = new Point((int) mouseWorldX, (int) mouseWorldY);
        }

    }

}
