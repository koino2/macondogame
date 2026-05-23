package scripts;

import lib.Script;

import java.awt.*;

public class DebugText extends Script {

    double fps;
    double fpsTimer = 0;
    int fpsFrames = 0;

    double ups;
    double upsTimer = 0;
    int upsFrames = 0;

    @Override
    public void start() {

    }

    long lastRender = System.nanoTime();
    double frameTime = 0;

    @Override
    public void update(double deltaTime) {
        upsTimer += deltaTime;
        upsFrames++;

        if (upsTimer >= 1.0) {
            ups = upsFrames;
            upsFrames = 0;
            upsTimer = 0;
        }
    }

    String rdt1 = "Object Render Time: ??? millis";
    String rdt2 = "Lighting Time: ??? millis ( +??? millis )";
    String rdt3 = "Post Processing Time: ??? millis ( +??? millis )";
    String rdt4 = "Final Render Time: ??? millis ( +??? millis )";

    @Override
    public void renderUI(Graphics g) {
        fpsTimer += frameTime;
        fpsFrames++;

        if (fpsTimer >= 1.0) {
            fps = fpsFrames;
            fpsFrames = 0;
            fpsTimer = 0;

            rdt1 = ("Object Render Time: "+(object.scene.objectRenderTime/1_000_000)+" millis.");
            rdt2 = ("Lighting Time: "+(object.scene.lightingTime/1_000_000)+" millis. ( +"+((object.scene.lightingTime-object.scene.objectRenderTime)/1_000_000)+" millis )");
            rdt3 = ("Post Processing Time:  "+(object.scene.postProcessingTime/1_000_000)+ " millis. ( +"+((object.scene.postProcessingTime-object.scene.lightingTime)/1_000_000)+" millis )");
            rdt4 = ("Final Render Time: "+(object.scene.finalRenderTime/1_000_000)+" millis. ( +"+((object.scene.finalRenderTime-object.scene.postProcessingTime)/1_000_000)+" millis )");
        }

        g.setColor(Color.white);
        g.setFont(new Font("Segoe UI Light", Font.PLAIN, 25));
        g.drawString("FPS: "+fps, 20, 35);
        //g.setColor(Color.black);
        //g.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        //g.drawString("FPS: "+fps, 21, 36);

        frameTime = (System.nanoTime() - lastRender)/1_000_000_000.0;
        lastRender = System.nanoTime();

        g.drawString("UPS: "+ ups, 20, 70);

        g.setFont(new Font("Segoe UI Light", Font.PLAIN, 15));
        g.drawString(rdt1, 20, 100);
        g.drawString(rdt2, 20, 120);
        g.drawString(rdt3, 20, 140);
        g.drawString(rdt4, 20, 160);
    }
}