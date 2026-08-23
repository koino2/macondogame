package game.levels;

import lib.*;
import lib.postProcessEffects.Vignette;

import java.awt.*;
import java.util.Random;

public class LoseScreen extends Scene {
    @Override
    public void start() {
        Object2D text = new Object2D(0, 0, 1, 1, 0);
        text.texture = StaticTextures.read("src/assets/textures/objects/screen/win.png");
        text.xSize = text.texture.getWidth();
        text.ySize = text.texture.getHeight();
        addObject(text);

        Vignette vignette = new Vignette();
        postProcessEffects.add(vignette);

        String path = "src/assets/textures/objects/background/1.png";
        Random random = new Random();
        int num = random.nextInt(0, 4);
        if (num == 0){path = "src/assets/textures/objects/background/1.png";}
        if (num == 1){path = "src/assets/textures/objects/background/2.png";}
        if (num == 2){path = "src/assets/textures/objects/background/2.png";}
        if (num == 3){path = "src/assets/textures/objects/background/3.png";}
        Object2D sky = new Object2D(0, 0, 1, 1, 0);
        sky.texture = StaticTextures.read(path);
        sky.xSize = sky.texture.getWidth();
        sky.ySize = sky.texture.getHeight();
        sky.zIndex = -1000;
        sky.tags.add("noCollision");
        sky.color = new Color(150, 150, 150);
        sky.addScript(new Script() {

            private final String[] backgrounds = {
                    "src/assets/textures/objects/background/1.png",
                    "src/assets/textures/objects/background/2.png",
                    "src/assets/textures/objects/background/3.png"
            };

            private final Random random = new Random();

            private float timer = 0;

            private final float holdTime = 5f;
            private final float fadeTime = 1.5f;

            private int currentBackground;

            private enum Phase {
                HOLD,
                FADE_OUT,
                FADE_IN
            }

            private Phase phase = Phase.HOLD;

            @Override
            public void start() {
                currentBackground = random.nextInt(backgrounds.length);
                object.texture = StaticTextures.read(backgrounds[currentBackground]);
                object.setColor(new Color(150, 150, 150));
            }

            @Override
            public void update(double deltaTime) {
                timer += (float) deltaTime;

                switch (phase) {

                    case HOLD:
                        if (timer >= holdTime) {
                            timer = 0;
                            phase = Phase.FADE_OUT;
                        }
                        break;

                    case FADE_OUT: {
                        float progress = Math.min(timer / fadeTime, 1f);

                        int brightness = (int) (150 * (1f - progress));

                        object.setColor(new Color(
                                brightness,
                                brightness,
                                brightness
                        ));

                        if (progress >= 1f) {

                            int next;
                            do {
                                next = random.nextInt(backgrounds.length);
                            } while (next == currentBackground && backgrounds.length > 1);

                            currentBackground = next;

                            object.texture =
                                    StaticTextures.read(backgrounds[currentBackground]);

                            timer = 0;
                            phase = Phase.FADE_IN;
                        }

                        break;
                    }

                    case FADE_IN: {
                        float progress = Math.min(timer / fadeTime, 1f);

                        int brightness = (int) (150 * progress);

                        object.setColor(new Color(
                                brightness,
                                brightness,
                                brightness
                        ));

                        if (progress >= 1f) {
                            timer = 0;
                            phase = Phase.HOLD;
                        }

                        break;
                    }
                }
            }
        });
        addObject(sky);

        camera.addScript(new Script() {
            @Override
            public void start() {

            }

            @Override
            public void update(double deltaTime) {
                float xP = (float) Input.getMousePosition().x / engine.getWidth();
                float yP = (float) Input.getMousePosition().y / engine.getHeight();

                object.xPos = (xP-0.5f)*20;
                object.yPos = (yP-0.5f)*20;
                System.out.println(object.scripts.size());
            }
        });
        addObject(camera);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void renderUI(Graphics g) {

    }
}
