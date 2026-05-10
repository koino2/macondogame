package lib.postProcessEffects;

import lib.PostProcessEffect;
import lib.spam;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ChromaticAberration extends PostProcessEffect {

    int strength = 10;

    @Override
    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int[] input = ((DataBufferInt) (image.getRaster().getDataBuffer())).getData();
        int[] out = ((DataBufferInt) (output.getRaster().getDataBuffer())).getData();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float centerX = width / 2f;
                float centerY = height / 2f;

                float dx = x - centerX;
                float dy = y - centerY;

                float distanceFromCenter = (float) Math.sqrt(dx * dx + dy * dy);
                float dist = (float) distanceFromCenter / Math.max(width, height);
                float offset = dist * strength;

                int rx = (int) Math.max(0, Math.min(width-1, x-offset));
                int bx = (int) Math.max(0, Math.min(width-1, x+offset));

                int rRGB = input[rx + y * width];
                int gRGB = input[x + y * width];
                int bRGB = input[bx + y * width];

                int r = (rRGB >> 16) & 0xFF;
                int g = (gRGB >> 8) & 0xFF;
                int b = bRGB & 0xFF;

                out[x+y*width] = spam.convertToARGB(255,r,g,b);
            }
        }
        return output;
    }
}
