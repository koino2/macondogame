package lib.postProcessEffects;

import lib.PostProcessEffect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Vignette extends PostProcessEffect {
    public Color color = Color.BLACK;
    public float strength = 1f;
    public float size = 0.2f;
    public float radiusMultiplier = 1f;

    @Override
    public BufferedImage apply(BufferedImage image) {
        Graphics2D g = (Graphics2D) image.createGraphics();

        int width = image.getWidth();
        int height = image.getHeight();

        float radius = Math.max(width, height) * radiusMultiplier;

        Color edgeColor = new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                (int)Math.min((color.getAlpha() * strength), 255)
        );

        RadialGradientPaint paint = new RadialGradientPaint(
                new Point(width / 2, height / 2),
                Math.max(1, radius),
                new float[]{0f, size, 1f},
                new Color[]{
                        new Color(0,0,0,0),
                        new Color(0,0,0,0),
                        edgeColor
                }
        );
        g.setPaint(paint);
        g.fillRect(0,0,image.getWidth(), image.getHeight());

        g.dispose();
        return image;
    }
}
