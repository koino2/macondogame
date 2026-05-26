package scripts;

import lib.Camera;
import lib.Input;
import lib.Object2D;
import lib.Script;

import java.awt.*;

public class CameraController extends Script {
    public Object2D target;

    public float scrollSpeed = 1.2f;
    public float minZoom = 0.5f;
    public float maxZoom = 2f;
    public float scrollSmoothing = 10f;
    public float targetScale = 1f;

    public float targetX;
    public float targetY;
    public float moveSmoothing = 5f;

    public CameraController(Object2D target){
        this.target = target;
    }

    @Override
    public void start() {
        object.scene.camera.xPos = target.xPos;
        object.scene.camera.yPos = target.yPos;
    }

    @Override
    public void update(double deltaTime) {
        float scrollDelta = Input.scrollDelta;

        if(scrollDelta < 0){
            targetScale *= scrollSpeed;
        } else if (scrollDelta > 0){
            targetScale /= scrollSpeed;
        }

        if(targetScale < minZoom){
            targetScale = minZoom;
        }

        if(targetScale > maxZoom){
            targetScale = maxZoom;
        }

        Camera camera = object.scene.camera;

        camera.scale += (targetScale - camera.scale) * (1f - (float)Math.exp(-scrollSmoothing * deltaTime));

        targetX = target.globalX;
        targetY = target.globalY;

        camera.xPos += (targetX - camera.xPos) * (1f - (float)Math.exp(-moveSmoothing * deltaTime));
        camera.yPos += (targetY - camera.yPos) * (1f - (float)Math.exp(-moveSmoothing * deltaTime));
    }

    @Override
    public void renderUI(Graphics g) {

    }
}
