package lib.particles;

import lib.Object2D;
import lib.Script;

import java.awt.*;
import java.util.Random;

public class ParticleEmitter extends Object2D {
    public double spawnTime = 0.5f;
    public float speed = 100;
    public float acceleration = 50f;

    Random rng = new Random();

    public Object2D particleHolder = this;

    public int particlesSpawned = 0;

    public static Point getPointInDirection(float rotation, float distance){
        double rad = Math.toRadians(rotation);

        return new Point(
                (int) (Math.cos(rad) * distance),
                (int) (Math.sin(rad) * distance)
        );
    }

    public Object2D particle(float x, float y){
        Object2D obj = new Object2D(x, y, 20, 20 ,0);

        ParticleScript script = new ParticleScript();
        obj.addScript(script);

        return obj;
    }

    public ParticleEmitter(float x, float y) {
        super(x, y, 0, 0, 0);
        addScript(new Script() {
            @Override
            public void start() {

            }

            double time = 0;
            double timeSinceLastSpawn = 0;

            @Override
            public void update(double deltaTime) {
                time += deltaTime;
                timeSinceLastSpawn += deltaTime;

                if (timeSinceLastSpawn > spawnTime){
                    Object2D particle = particle(x, y);
                    Point dir = getPointInDirection(rng.nextInt(0, 360), speed);
                    particle.xVelocity = dir.x;
                    particle.yVelocity = dir.y;
                    particle.xAcceleration = acceleration * dir.x / speed;
                    particle.yAcceleration = acceleration * dir.y / speed;
                    particleHolder.addChild(particle);

                    particlesSpawned += 1;
                    timeSinceLastSpawn = 0;
                }
            }
        });
    }
}
