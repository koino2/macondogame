package game.prefabs.explosion;

import game.levels.Level;
import game.scripts.weapons.flamethrower.FlamethrowerParticleScript;
import lib.Light;
import lib.Object2D;
import lib.Script;
import lib.particles.ParticleEmitter;
import lib.particles.ParticleScript;

import java.awt.*;
import java.util.Random;

public class ExplosionParticle extends ParticleEmitter {

    public Random rng = new Random();

    public ExplosionParticle(){
        super(0,0);
        spawnTime = 0.05f;
        spread = 30;
        speed = 200;

        addScript(new Script() {
            @Override
            public void start() {
                particleHolder = ((Level)(scene)).trash;
            }

            @Override
            public void update(double deltaTime) {

            }
        });
    }

    public Color randomColor(Color color, int randomness){
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        r += rng.nextInt(-randomness, randomness);
        g += rng.nextInt(-randomness, randomness);
        b += rng.nextInt(-randomness, randomness);

        if (r > 255) r = 255;
        if (g > 255) g = 255;
        if (b > 255) b = 255;

        if (r < 0) r = 0;
        if (g < 0) g = 0;
        if (b < 0) b = 0;

        return new Color(r, g, b);
    }

    public Object2D particle(float x, float y){
        Object2D obj = new Object2D(x, y, 20, 20 ,0);

        obj.color = randomColor(new Color(244, 140, 36), 50);

        ParticleScript script = new ParticleScript();
        script.lifetime = 5;
        obj.addScript(script);

        obj.addScript(new FlamethrowerParticleScript());

        obj.xPos += globalX;
        obj.yPos += globalY;

        return obj;
    }
}
