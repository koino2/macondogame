package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

public abstract class Scene {
    public Engine engine;

    public List<Object2D> objects = new ArrayList<>();
    public List<Light> lights = new ArrayList<>();
    public List<PostProcessEffect> postProcessEffects = new ArrayList<>();
    public boolean postProcessingEnabled = true;

    public boolean started = false;

    public abstract void start();
    public abstract void update(double deltaTime);
    public abstract void renderUI(Graphics g);

    public void startObjects(){
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).start();
        }
    }
    public void updateObjects(double deltaTime){
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update(deltaTime);
        }
    }
    public void renderUIObjects(Graphics g){
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).renderUI(g);
        }
    }

    public void addObject(Object2D object){
        objects.add(object);
        object.scene = this;

        if(started){
            object.start();
        }
    }

    public Color ambientColor = new Color(255, 255, 255);

    public void render(Graphics g) {
        //double startTime = System.nanoTime();

        BufferedImage sceneBuffer = new BufferedImage(engine.getWidth(), engine.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bufferGraphics = sceneBuffer.createGraphics();

        objects.sort(Comparator.comparingInt(o -> o.zIndex));
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).render(bufferGraphics);
        }

        //double objectRenderTime = System.nanoTime() - startTime;

        BufferedImage lightMap = new BufferedImage(engine.getWidth(), engine.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D lg = lightMap.createGraphics();

        lg.setColor(ambientColor);
        lg.fillRect(0,0,engine.getWidth(), engine.getHeight());
        lg.setComposite(AlphaComposite.SrcOver);

        for (int i = 0; i < lights.size(); i++) {
            Light light = lights.get(i);
            RadialGradientPaint paint = new RadialGradientPaint(
                    light.x,
                    light.y,
                    light.radius,
                    new float[]{0,1},
                    new Color[]{
                            light.color,
                            ambientColor
                    }
            );
            lg.setPaint(paint);
            lg.fillOval((int) (light.x- light.radius), (int) (light.y-light.radius), (int) (light.radius*2), (int) (light.radius*2));
        }

        BufferedImage finalImage = new BufferedImage(engine.getWidth(), engine.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < finalImage.getHeight(); y++) {
            for (int x = 0; x < finalImage.getWidth(); x++) {
                int sceneRGB = sceneBuffer.getRGB(x,y);
                int lightRGB = lightMap.getRGB(x,y);

                int sceneA = (sceneRGB >> 24) & 0xFF;
                int sceneR = (sceneRGB >> 16) & 0xFF;
                int sceneG = (sceneRGB >> 8) & 0xFF;
                int sceneB = sceneRGB & 0xFF;

                int lightA = (lightRGB >> 24) & 0xFF;
                int lightR = (lightRGB >> 16) & 0xFF;
                int lightG = (lightRGB >> 8) & 0xFF;
                int lightB = lightRGB & 0xFF;

                int finalR = sceneR * lightR / 255;
                int finalG = sceneG * lightG / 255;
                int finalB = sceneB * lightB / 255;

                int finalRGB = (255 << 24) | (finalR << 16) | (finalG << 8) | finalB;

                finalImage.setRGB(x,y, finalRGB);
            }
        }

        //double lightingTime = System.nanoTime() - startTime;

        if(postProcessingEnabled) {
            postProcessEffects.sort(Comparator.comparingInt(p -> p.priority));
            for (int i = 0; i < postProcessEffects.size(); i++) {
                if (postProcessEffects.get(i).enabled == true) {
                    finalImage = postProcessEffects.get(i).apply(finalImage);
                }
            }
        }

        //double postProcessingTime = System.nanoTime() - startTime;

        g.drawImage(finalImage, 0, 0, null);

        //double finalRenderTime = System.nanoTime() - startTime;

        //System.out.println("Object Render Time: "+(objectRenderTime/1_000_000)+" millis.");
        //System.out.println("Lighting Time: "+(lightingTime/1_000_000)+" millis. ( +"+((lightingTime-objectRenderTime)/1_000_000)+" millis )");
        //System.out.println("Post Processing Time:  "+(postProcessingTime/1_000_000)+ " millis. ( +"+((postProcessingTime-lightingTime)/1_000_000)+" millis )");
        //System.out.println("Final Render Time: "+(finalRenderTime/1_000_000)+" millis. ( +"+((finalRenderTime-postProcessingTime)/1_000_000)+" millis )");
        //System.out.println("-----------------------------------------------------");
    }
}