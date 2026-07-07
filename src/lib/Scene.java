package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

public abstract class Scene {
    public Engine engine;

    public Camera camera;
    public List<Object2D> objects = new ArrayList<>();
    private List<Light> lights = new ArrayList<>();
    public List<PostProcessEffect> postProcessEffects = new ArrayList<>();
    public boolean postProcessingEnabled = true;

    public Color ambientColor = new Color(255, 255, 255);

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

    public void updateLights(){
        lights.clear();
        for (int i = 0; i < objects.size(); i++) {
            List<Object2D> descendants = objects.get(i).getDescendants();
            for (int j = 0; j < descendants.size(); j++) {
                if(descendants.get(j) instanceof Light){
                    lights.add((Light) descendants.get(j));
                }
            }
        }
    }

    public void destroy(){
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).destroy();
        }
    }

    public double startTime;
    public double objectRenderTime;
    public double lightingTime;
    public double postProcessingTime;
    public double finalRenderTime;

    public void render(Graphics g) {
        updateLights();

        if(camera == null){
            camera = new Camera(0,0,0);
        }

        startTime = System.nanoTime();
        int width = engine.getWidth();
        int height = engine.getHeight();

        BufferedImage sceneBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bufferGraphics = sceneBuffer.createGraphics();

        bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        bufferGraphics.setColor(ambientColor);
        bufferGraphics.fillRect(0,0,engine.getWidth(), engine.getHeight());

        bufferGraphics.translate(width/2, height/2);
        bufferGraphics.scale(camera.scale, camera.scale);
        bufferGraphics.translate(-camera.globalX, -camera.globalY);

        objects.sort(Comparator.comparingInt(o -> o.zIndex));
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).render(bufferGraphics);
        }

        objectRenderTime = System.nanoTime() - startTime;

        BufferedImage lightMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D lg = lightMap.createGraphics();

        lg.setColor(ambientColor);
        lg.fillRect(0,0,engine.getWidth(), engine.getHeight());

        lg.translate(width/2, height/2);
        lg.scale(camera.scale, camera.scale);
        lg.translate(-camera.globalX, -camera.globalY);

        lg.setComposite(AlphaComposite.SrcOver);

        for (int i = 0; i < lights.size(); i++) {
            Light light = lights.get(i);
            RadialGradientPaint paint = new RadialGradientPaint(
                    light.globalX,
                    light.globalY,
                    light.radius,
                    new float[]{0,1},
                    new Color[]{
                            light.color,
                            new Color(light.color.getRed(), light.color.getGreen(), light.color.getBlue(), 0)
                    }
            );
            lg.setPaint(paint);
            lg.fillOval((int) (light.globalX- light.radius), (int) (light.globalY-light.radius), (int) (light.radius*2), (int) (light.radius*2));
        }

        BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] finalPixels = ((DataBufferInt)(finalImage.getRaster().getDataBuffer())).getData();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int sceneRGB = sceneBuffer.getRGB(x,y);
                int lightRGB = lightMap.getRGB(x,y);

                int sceneR = (sceneRGB >> 16) & 0xFF;
                int sceneG = (sceneRGB >> 8) & 0xFF;
                int sceneB = sceneRGB & 0xFF;

                int lightR = (lightRGB >> 16) & 0xFF;
                int lightG = (lightRGB >> 8) & 0xFF;
                int lightB = lightRGB & 0xFF;

                int finalR = sceneR * lightR / 255;
                int finalG = sceneG * lightG / 255;
                int finalB = sceneB * lightB / 255;

                int finalRGB = (255 << 24) | (finalR << 16) | (finalG << 8) | finalB;

                finalPixels[x+y*finalImage.getWidth()] = finalRGB;
            }
        }

        lightingTime = System.nanoTime() - startTime;

        if(postProcessingEnabled) {
            postProcessEffects.sort(Comparator.comparingInt(p -> p.priority));
            for (int i = 0; i < postProcessEffects.size(); i++) {
                if (postProcessEffects.get(i).enabled == true) {
                    finalImage = postProcessEffects.get(i).apply(finalImage);
                }
            }
        }

        postProcessingTime = System.nanoTime() - startTime;

        g.drawImage(finalImage, 0, 0, null);

        finalRenderTime = System.nanoTime() - startTime;

        //renderDebugText1 = ("Object Render Time: "+(objectRenderTime/1_000_000)+" millis.");
        //renderDebugText2 = ("Lighting Time: "+(lightingTime/1_000_000)+" millis. ( +"+((lightingTime-objectRenderTime)/1_000_000)+" millis )");
        //renderDebugText3 = ("Post Processing Time:  "+(postProcessingTime/1_000_000)+ " millis. ( +"+((postProcessingTime-lightingTime)/1_000_000)+" millis )");
        //renderDebugText4 = ("Final Render Time: "+(finalRenderTime/1_000_000)+" millis. ( +"+((finalRenderTime-postProcessingTime)/1_000_000)+" millis )");
        //System.out.println("-----------------------------------------------------");
    }

}