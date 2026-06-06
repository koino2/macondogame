package lib;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.List;

public class Object2D {
    public float globalX;
    public float globalY;
    public float xPos;
    public float yPos;
    public float xSize;
    public float ySize;
    public float globalRotation;
    public float rotation;

    public List<String> tags = new ArrayList<>();

    public List<Sound> sounds = new ArrayList<>();

    public Object2D parent;
    public List<Object2D> children = new ArrayList<>();

    public boolean destroyed = false;

    public BufferedImage texture = StaticTextures.square();
    public Color color = new Color(255, 255, 255);
    public int zIndex = 0;

    public float xVelocity;
    public float yVelocity;
    public float xAcceleration;
    public float yAcceleration;

    public Scene scene;
    public List<Script> scripts = new ArrayList<>();

    public Object2D(float x, float y, float xSize, float ySize, float rotation){
        this.xPos = x;
        this.yPos = y;
        this.xSize = xSize;
        this.ySize = ySize;
        this.rotation = rotation;
    }

    public void start(){
        for (int i = 0; i < scripts.size(); i++) {
            scripts.get(i).start();
            scripts.get(i).started = true;
        }
    }

    public void update(double deltaTime){

        if(destroyed){
            for (int i = 0; i < sounds.size(); i++) {
                sounds.get(i).stop();
            }

            for (int i = 0; i < children.size(); i++) {
                children.get(i).destroy();
            }

            if(parent != null){
                parent.children.remove(this);
            }

            if(scene != null) {
                scene.objects.remove(this);
            }

            for (int i = 0; i < scripts.size(); i++) {
                scripts.get(i).onDestroy();
            }
        }

        if(parent != null){
            globalX = parent.globalX + xPos;
            globalY = parent.globalY + yPos;
            globalRotation = parent.globalRotation + rotation;
        } else{
            globalX = xPos;
            globalY = yPos;
            globalRotation = rotation;
        }
        for (int i = 0; i < children.size(); i++) {
            children.get(i).update(deltaTime);
        }

        xVelocity += (float) (xAcceleration * deltaTime);
        yVelocity += (float) (yAcceleration * deltaTime);

        xPos += (float) (xVelocity * deltaTime);
        yPos += (float) (yVelocity * deltaTime);

        for (int i = 0; i < scripts.size(); i++) {
            scripts.get(i).update(deltaTime);
        }
    }

    public void renderUI(Graphics g){
        for (int i = 0; i < scripts.size(); i++) {
            scripts.get(i).renderUI(g);
        }
    }

    public void addScript(Script script){
        scripts.add(script);
        script.object = this;
    }

    public void destroy(){
        destroyed = true;
    }

    public List<Object2D> getDescendants(){
        List<Object2D> descendants = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            descendants.add(children.get(i));
            descendants.addAll(children.get(i).getDescendants());
        }
        return descendants;
    }

    public void addChild(Object2D child){
        children.add(child);
        child.parent = this;
        child.scene = scene;
        if(!scene.objects.contains(child)) {
            scene.addObject(child);
        }
    }

    RescaleOp op;
    void refreshOP(){
        op = new RescaleOp(
                new float[]{(float) color.getRed() /255, (float) color.getGreen() /255, (float) color.getBlue() /255, (float) color.getAlpha() /255},
                new float[4],
                null
        );
    }
    public void setColor(Color newColor){
        this.color = newColor;
        refreshOP();
    }

    public void render(Graphics2D g){
        if(op == null){
            refreshOP();
        }
        AffineTransform old = g.getTransform();
        g.translate(globalX,globalY);
        g.rotate(Math.toRadians(globalRotation));

        BufferedImage tinted = op.filter(texture, null);
        g.drawImage(tinted, (int) (-xSize/2), (int) (-ySize/2), (int) xSize, (int) ySize, null);
        g.setTransform(old);
    }

    public boolean intersects(Object2D other){
        Point2D.Float[] a = this.getVertices();
        Point2D.Float[] b = other.getVertices();

        return checkAxes(a,b) && checkAxes(b,a);
    }

    // ugly sat collision detection stuff
    // |  |  |  |  |  |  |  |  |  |  |  |
    // V  V  V  V  V  V  V  V  V  V  V  V

    public Point2D.Float getMTV(Object2D other){
        Point2D.Float[] a = this.getVertices();
        Point2D.Float[] b = other.getVertices();

        float smallestOverlap = Float.MAX_VALUE;
        float mtvX = 0;
        float mtvY = 0;

        for (int pass = 0; pass < 2; pass++) {
            Point2D.Float[] verts = (pass == 0)? a : b;

            for (int i = 0; i < 4; i++) {
                Point2D.Float p1 = verts[i];
                Point2D.Float p2 = verts[(i+1)%4];

                float edgeX = p2.x - p1.x;
                float edgeY = p2.y - p1.y;

                float axisX = -edgeY;
                float axisY = edgeX;

                float len = (float)Math.sqrt(axisX*axisX + axisY*axisY);
                if (len == 0) continue;

                axisX /= len;
                axisY /= len;

                float[] projA = project(a, axisX, axisY);
                float[] projB = project(b, axisX, axisY);

                float overlap = Math.min(projA[1],projB[1]) - Math.max(projA[0], projB[0]);

                if(overlap <= 0){
                    return null;
                }

                if(overlap < smallestOverlap){
                    smallestOverlap = overlap;
                    mtvX = axisX;
                    mtvY = axisY;
                }
            }
        }

        float dx = other.globalX - this.globalX;
        float dy = other.globalY - this.globalY;

        if(dx * mtvX + dy * mtvY < 0){
            mtvX = -mtvX;
            mtvY = -mtvY;
        }

        return new Point2D.Float(mtvX * smallestOverlap, mtvY * smallestOverlap);
    }

    float[] project(Point2D.Float[] verts, float axisX, float axisY) {
        float min = dot(verts[0], axisX, axisY);
        float max = min;

        for (int i = 1; i < verts.length; i++) {
            float p = dot(verts[i], axisX, axisY);
            if(p < min){
                min = p;
            }
            if(p > max){
                max = p;
            }
        }

        return new float[]{min,max};
    }

    float dot(Point2D.Float p, float ax, float ay){
        return  p.x * ax + p.y * ay;
    }

    public Point2D.Float[] getVertices(){
        float halfWidth = xSize/2;
        float halfHeight = ySize/2;

        float[][] local = {
                {-halfWidth, -halfHeight},
                { halfWidth, -halfHeight},
                { halfWidth,  halfHeight},
                {-halfWidth,  halfHeight}
        };

        float rad = (float) Math.toRadians(globalRotation);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        Point2D.Float[] vertices = new Point2D.Float[4];

        for (int i = 0; i < local.length; i++) {
            float localX = local[i][0];
            float localY = local[i][1];

            float rx = localX * cos - localY * sin;
            float ry = localX * sin + localY * cos;

            vertices[i] = new Point2D.Float(rx + globalX, ry + globalY);
        }

        return vertices;
    }

    public boolean checkAxes(Point2D.Float[] a, Point2D.Float[] b){
        for (int i = 0; i < 4; i++) {
            Point2D.Float p1 = a[i];
            Point2D.Float p2 = a[(i+1)%4];

            float edgeX = p2.x - p1.x;
            float edgeY = p2.y - p1.y;

            float axisX = -edgeY;
            float axisY = edgeX;

            float[] projA = project(a, axisX, axisY);
            float[] projB = project(b, axisX, axisY);

            float overlap = Math.min(projA[1],projB[1]) - Math.max(projA[0], projB[0]);

            if(overlap <= 0){
                return false;
            }
        }
        return true;
    }
}
