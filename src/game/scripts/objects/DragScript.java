package game.scripts.objects;

import lib.Input;
import lib.Script;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DragScript extends Script {

    @Override
    public void start() {

    }

    public boolean dragging = false;
    public float dragX = 0;
    public float dragY = 0;

    public boolean canDrag = true;

    public void onDragStart(){}
    public void onDragEnd(){}

    @Override
    public void update(double deltaTime) {
        Point mouse = getMouseWorldPosition();

        if (mouse == null) return;
        int globalX = mouse.x;
        int globalY = mouse.y;

        boolean mouseDown = Input.isMouseDown(MouseEvent.BUTTON1);

        if (mouseDown) {
            if (!dragging) {
                if (globalX > object.globalX - object.xSize / 2 && globalX < object.globalX + object.xSize / 2
                        && globalY > object.globalY - object.ySize / 2 && globalY < object.globalY + object.ySize / 2
                        && canDrag
                ) {
                    dragging = true;
                    dragX = globalX - object.globalX;
                    dragY = globalY - object.globalY;

                    onDragStart();
                }
            }
            if (dragging){
                object.xPos = mouse.x - dragX;
                object.yPos = mouse.y - dragY;
            }
        }

        else {
            if (dragging){
                dragging = false;
                onDragEnd();
            }
        }

    }

    public Point getMouseWorldPosition(){
        Point point = Input.getMousePosition();
        float mouseWorldX;
        float mouseWorldY;
        if (point != null) {
            mouseWorldX = (float)((point.x-object.scene.engine.getWidth()/2.0)/object.scene.camera.scale+object.scene.camera.globalX);
            mouseWorldY = (float)((point.y-object.scene.engine.getHeight()/2.0)/object.scene.camera.scale+object.scene.camera.globalY);
            return new Point((int) mouseWorldX, (int) mouseWorldY);
        }
        return null;
    }
}
