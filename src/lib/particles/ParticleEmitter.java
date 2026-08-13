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

    public boolean enabled = true;

    public float direction = 0;
    public float spread = 360;

    public float speedRandomness = 20;

    public int particlesPerSpawn = 1;

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
                timeSinceLastSpawn = spawnTime;
            }

            double time = 0;
            double timeSinceLastSpawn = 0;

            @Override
            public void update(double deltaTime) {
                time += deltaTime;
                timeSinceLastSpawn += deltaTime;

                if (timeSinceLastSpawn > spawnTime && enabled){
                    for (int i = 0; i < particlesPerSpawn; i++) {
                        Object2D particle = particle(x, y);

                        float angle = direction + rng.nextFloat(-spread / 2f, spread / 2f);
                        Point dir = getPointInDirection(angle, speed);

                        particle.xVelocity = dir.x;
                        particle.yVelocity = dir.y;
                        particle.xAcceleration = (acceleration * dir.x / speed) + rng.nextFloat(-speedRandomness, speedRandomness);
                        particle.yAcceleration = (acceleration * dir.y / speed) + rng.nextFloat(-speedRandomness, speedRandomness);
                        particleHolder.addChild(particle);

                        particlesSpawned += 1;
                    }
                    timeSinceLastSpawn = 0;
                }
            }
        });
    }
}
