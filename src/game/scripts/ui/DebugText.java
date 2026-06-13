package game.scripts.ui;

import game.levels.Level;
import lib.Input;
import lib.Script;

import java.awt.*;
import java.awt.event.KeyEvent;

public class DebugText extends Script {

    double fps;
    double fpsTimer = 0;
    int fpsFrames = 0;

    double ups;
    double upsTimer = 0;
    int upsFrames = 0;

    Runtime runtime;

    boolean on = false;

    public DebugText(){
        runtime = Runtime.getRuntime();
    }

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

        time += deltaTime;
    }

    String rdt1 = "Object Render Time: ??? millis";
    String rdt2 = "Lighting Time: ??? millis ( +??? millis )";
    String rdt3 = "Post Processing Time: ??? millis ( +??? millis )";
    String rdt4 = "Final Render Time: ??? millis ( +??? millis )";

    boolean pressed = false;

    double time = 0;

    public Color textColor = Color.white;

    @Override
    public void renderUI(Graphics g){

        if(Input.isKeyDown(KeyEvent.VK_F3) && !pressed){
            on = !on;
            pressed = true;
        }
        if(pressed && !Input.isKeyDown(KeyEvent.VK_F3)){
            pressed = false;
        }

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

        frameTime = (System.nanoTime() - lastRender)/1_000_000_000.0;
        lastRender = System.nanoTime();

        g.setColor(textColor);

        String posXtext = "xPos: ???";
        String posYtext = "yPos: ???";

        if(object.scene instanceof Level && ((Level)(object.scene)).player != null){
            posXtext = String.format("xPos: %.1f",((Level)(object.scene)).player.xPos);
            posYtext = String.format("yPos: %.1f",((Level)(object.scene)).player.yPos);
        }

        TextSection[] ts = new TextSection[]{

                new TextSection(new String[]{
                        "FPS: "+fps,
                        "UPS: "+ups
                }, "Segoe UI Bold", new int[]{Font.BOLD}, 25, 5),

                new TextSection(new String[]{
                        rdt1,
                        rdt2,
                        rdt3,
                        rdt4
                }, "Segoe UI Semilight", new int[]{Font.PLAIN}, 15, 0),

                new TextSection(new String[]{
                        posXtext,
                        posYtext
                }, "Segoe UI Semilight", new int[]{Font.PLAIN}, 20, 5),

                new TextSection(new String[]{
                        "JVM Max Memory: "+(runtime.maxMemory()/(1024*1024)) + " MB",
                        "JVM Free Memory: "+((runtime.freeMemory())/(1024*1024)) + " MB",
                        "JVM Total Memory: "+(runtime.totalMemory()/(1024*1024)) + " MB",
                }, "Segoe UI Semilight", new int[]{Font.PLAIN}, 10, 5),

                new TextSection(new String[]{
                        "JVM Used Memory: "+((runtime.totalMemory()- runtime.freeMemory())/(1024*1024)) + " MB"
                }, "Segoe UI Semilight", new int[]{Font.PLAIN}, 15, 5),

                new TextSection(new String[]{
                        "Objects: "+object.scene.objects.size()
                }, "Segoe UI Semilight", new int[]{Font.PLAIN}, 20, 5),

                new TextSection(new String[]{
                        String.format("Time: %.2f", time)
                }, "Segoe UI Semilight", new int[]{Font.PLAIN}, 20, 5),
        };

        int y = 40;
        int sectionPadding = 30;

        if(on) {

            for (int i = 0; i < ts.length; i++) {
                ts[i].render(g, 20, y);
                y += ts[i].endPixel;
                y += sectionPadding;
            }

        }
    }

    /*@Override
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

        g.setFont(new Font("Segoe UI Light", Font.PLAIN, 18));
        g.drawString(String.format("xPos: %.1f",object.xPos), 20, 190);
        g.drawString(String.format("yPos: %.1f",object.yPos), 20, 220);


        g.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
        g.drawString("JVM Max Memory: "+(runtime.maxMemory()/(1024*1024)) + " MB", 20, 250);
        g.drawString("JVM Free Memory: "+((runtime.freeMemory())/(1024*1024)) + " MB", 20, 270);
        g.drawString("JVM Total Memory: "+(runtime.totalMemory()/(1024*1024)) + " MB", 20, 290);
        g.drawString("JVM Used Memory: "+((runtime.totalMemory()- runtime.freeMemory())/(1024*1024)) + " MB", 20, 310);
    }*/

    public class TextSection{
        String[] texts;
        int fontSize;
        int padding;

        String fontName = "Segoe UI Light";

        int[] style;

        public int endPixel = 0;

        public TextSection(String[] texts, String fontName, int[] style, int fontSize, int padding){
            this.texts = texts;
            this.fontSize = fontSize;
            this.padding = padding;
            this.fontName = fontName;
            this.style = style;
        }

        public void render(Graphics g, int x, int y){
            for (int i = 0; i < texts.length; i++) {
                Font font = new Font(fontName, style[Math.min(i, style.length-1)], fontSize);
                g.setFont(font);
                FontMetrics fm = g.getFontMetrics(font);

                g.drawString(texts[i], x, (i* (fm.getHeight()+padding) )+y);
                endPixel = (i)* (fm.getHeight()+padding);
            }
        }
    }
}