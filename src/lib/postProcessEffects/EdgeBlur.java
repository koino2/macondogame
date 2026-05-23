package lib.postProcessEffects;

import lib.PostProcessEffect;
import lib.spam;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class EdgeBlur extends PostProcessEffect {
    int detectionRadius = 3;
    int blurRadius = 3;
    int threshold;
    @Override
    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] imagePixels = ((DataBufferInt)(image.getRaster().getDataBuffer())).getData();

        int[] temp = new int[width*height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int samples = 0;

                int originalColor = imagePixels[x+y*width];
                int originalR = spam.convertFromARGB(originalColor)[1];
                int originalG = spam.convertFromARGB(originalColor)[2];
                int originalB = spam.convertFromARGB(originalColor)[3];

                int totalR = 0;
                int totalG = 0;
                int totalB = 0;
                for (int sy = -detectionRadius; sy <= detectionRadius; sy++) {
                    for (int sx = -detectionRadius; sx <= detectionRadius; sx++) {

                        int px = x+sx;
                        int py = y+sy;

                        if(px >= width || px < 0 || py >= height || py < 0){continue;}
                        samples++;

                        int sampleColor = imagePixels[px+py*width];
                        int sampleR = spam.convertFromARGB(sampleColor)[1];
                        int sampleG = spam.convertFromARGB(sampleColor)[2];
                        int sampleB = spam.convertFromARGB(sampleColor)[3];

                        totalR += sampleR;
                        totalG += sampleG;
                        totalB += sampleB;

                        if(Math.abs(originalR - sampleR) > threshold){
                            temp[x+y*width] =
                            spam.convertToARGB(spam.convertFromARGB(originalColor)[0],
                                    totalR/samples,
                                    totalG/samples,
                                    totalB/samples)
                            ;
                        }
                    }
                }
            }
        }

        BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] finalPixels = ((DataBufferInt)(finalImage.getRaster().getDataBuffer())).getData();
        System.arraycopy(temp, 0, finalPixels, 0, temp.length);
        return finalImage;
    }
}
