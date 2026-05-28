package game.scripts.misc;

import lib.Script;

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
}
