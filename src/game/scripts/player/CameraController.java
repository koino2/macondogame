package game.scripts.player;

import lib.Camera;
import lib.Input;
import lib.Object2D;
import lib.Script;

import java.awt.*;
import java.util.Random;

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

    public float shakeTime = 0;
    public float shakeStrength = 0;

    public float shakeX = 0;
    public float shakeY = 0;

    Random rng = new Random();

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

        if (shakeTime > 0){
            shakeTime -= (float) deltaTime;
            float amount = shakeStrength * (shakeTime/0.2f);
            shakeX = rng.nextFloat(0, 1)*amount;
            shakeY = rng.nextFloat(0, 1)*amount;
        } else{
            shakeX = 0;
            shakeY = 0;
        }

        camera.xPos += shakeX;
        camera.yPos += shakeY;
    }

    @Override
    public void renderUI(Graphics g) {

    }

    public void shake(float strength, float duration){
        shakeStrength = Math.max(strength, shakeStrength);
        shakeTime = Math.max(duration, shakeStrength);
    }
}
