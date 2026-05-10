package lib;

import java.awt.image.BufferedImage;

public abstract class PostProcessEffect {
    public int priority = 0;
    public boolean enabled = true;
    public abstract BufferedImage apply(BufferedImage image);
}
