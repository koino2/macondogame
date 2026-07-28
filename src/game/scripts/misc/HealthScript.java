package game.scripts.misc;

import lib.Camera;
import lib.Engine;
import lib.Script;

import java.awt.*;

public abstract class HealthScript extends Script {
    public float maxHealth = 100;
    public float health = maxHealth;

    public float regenAmount = 1;
    public float regenInterval = 0.2f;
    public float regenBeginTimer = 5;

    public HealthScript(float maxHealth){
        this.maxHealth = maxHealth;
        health = maxHealth;
    }
    public HealthScript(float maxHealth, float health){
        this.maxHealth = maxHealth;
        this.health = health;
    }
    public HealthScript(){}

    @Override
    public void start() {

    }

    double timeSinceLastRegen = 0;
    double timeSinceLastDamage = 0;

    public void behaviour(double deltaTime){};

    @Override
    public void update(double deltaTime) {
        timeSinceLastRegen += deltaTime;
        timeSinceLastDamage += deltaTime;
        if(timeSinceLastRegen >= regenInterval && timeSinceLastDamage>=regenBeginTimer){
            health += regenAmount;
            timeSinceLastRegen = 0;
        }

        if(health > maxHealth){
            health = maxHealth;
        }
        behaviour(deltaTime);
    }

    public void onDamage(float damageAmount){}
    public void damage(float damageAmount){
        health -= damageAmount;
        timeSinceLastDamage = 0;
        onDamage(damageAmount);
    }

    @Override
    public void renderUI(Graphics g) {

        Engine engine = object.scene.engine;
        Camera camera = object.scene.camera;

        int barWidth = (int) (100 * camera.scale);
        int barHeight = (int) (10 * camera.scale);

        int xPos = (int)((object.globalX - camera.globalX) * camera.scale + (float) engine.getWidth() /2);
        xPos -= barWidth / 2;
        int yPos = (int)((object.globalY - camera.globalY - (object.ySize/2)*1.25f) * camera.scale + (float) engine.getHeight() /2);

        g.setColor(new Color(101, 26, 57));
        g.fillRect(xPos-5, yPos-(barHeight+5), barWidth+10, barHeight+10);

        g.setColor(new Color(33, 63, 76));
        g.fillRect(xPos, yPos-barHeight, barWidth, barHeight);
        g.setColor(new Color(255, 94, 94));
        g.fillRect(xPos, yPos-barHeight, (int) (barWidth*(health/maxHealth)), barHeight);

    }
}
