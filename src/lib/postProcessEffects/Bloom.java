package lib.postProcessEffects;

import lib.PostProcessEffect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Bloom extends PostProcessEffect {
    public float threshold = 0.3f;
    public int radius = 2;

    public float blurSizeMultiplier = 0.25f;

    private final float[] kernel = {
            0.06136f,
            0.24477f,
            0.38774f,
            0.24477f,
            0.06136f
    };

    @Override
    public BufferedImage apply(BufferedImage image) {

        int[] brightPass = new int[image.getHeight() * image.getWidth()];

        int[] imagePixels = ((java.awt.image.DataBufferInt)
                image.getRaster().getDataBuffer()
        ).getData();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int RGB = imagePixels[x + y * image.getWidth()];
                int R = (RGB >> 16) & 0xFF;
                int G = (RGB >> 8) & 0xFF;
                int B = RGB & 0xFF;
                int brightness = (R + G + B)/3;
                if(brightness > threshold*255){
                    brightPass[x + y * image.getWidth()] = RGB;
                }else{
                    brightPass[x + y * image.getWidth()] = 0;
                }
            }
        }

        int smallWidth = (int) (image.getWidth() * blurSizeMultiplier);
        int smallHeight = (int) (image.getHeight()*blurSizeMultiplier);

        BufferedImage smallBright = new BufferedImage(smallWidth, smallHeight, BufferedImage.TYPE_INT_ARGB);
        int[] smallBrightPixels = ((DataBufferInt)(smallBright.getRaster().getDataBuffer())).getData();

        for (int y = 0; y < smallHeight; y++) {
            for (int x = 0; x < smallWidth; x++) {
                int srcX = (int) (x / blurSizeMultiplier);
                int srcY = (int) (y / blurSizeMultiplier);

                smallBrightPixels[x+y*smallWidth] = brightPass[srcX+srcY*image.getWidth()];
            }
        }

        int[] blur = new int[smallWidth * smallHeight];

        int[] temp = new int[smallWidth * smallHeight];

        int threads = Runtime.getRuntime().availableProcessors();
        Thread[] workers = new Thread[threads];

        int rowsPerThread = smallHeight / threads;

        for (int i = 0; i < threads; i++) {
            int startY = i*rowsPerThread;
            int endY = (i == threads - 1) ? smallHeight : startY + rowsPerThread;
            workers[i] = new Thread(() -> {
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < smallWidth; x++) {
                        int totalR = 0;
                        int totalG = 0;
                        int totalB = 0;
                        int samples = 0;
                        for (int oy = -radius; oy <= radius; oy++) {
                            for (int ox = -radius; ox <= radius; ox++) {
                                int sx = x + ox;
                                int sy = y + oy;

                                if (sx < 0 || sy < 0 || sx >= smallWidth || sy >= smallHeight){
                                    continue;
                                }

                                int rgb = smallBrightPixels[sx + sy * smallWidth];

                                int r = (rgb >> 16) & 0xFF;
                                int g = (rgb >> 8) & 0xFF;
                                int b = rgb & 0xFF;

                                totalR += r;
                                totalG += g;
                                totalB += b;

                                samples++;
                            }
                        }

                        int finalR = totalR / samples;
                        int finalG = totalG / samples;
                        int finalB = totalB / samples;

                        int finalRGB =
                                (255 << 24) |
                                        (finalR << 16) |
                                        (finalG << 8) |
                                        finalB;

                        blur[x + y * smallWidth] = finalRGB;
                    }
                }
            });
            workers[i].start();
        }
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        /*float reduction = 3;
        int width = (int) Math.ceil(smallWidth/reduction);
        int height = (int) Math.ceil(smallWidth/reduction);
        BufferedImage blurImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D blurGraphics = blurImage.createGraphics();
        blurGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        blurGraphics.drawImage(smallBright, 0, 0, width, height, null);

        BufferedImage blurImageBig = new BufferedImage(smallWidth, smallHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bigBlurGraphics = blurImageBig.createGraphics();
        bigBlurGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        bigBlurGraphics.drawImage(blurImage, 0, 0, smallWidth, smallHeight, null);

        blur = ((DataBufferInt)(blurImageBig.getRaster().getDataBuffer())).getData();*/

        int[] finalPixels = new int[image.getWidth() * image.getHeight()];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int originalRGB = imagePixels[x + y * image.getWidth()];

                int blurX = (int) (x*blurSizeMultiplier);
                int blurY = (int) (y * blurSizeMultiplier);

                if(blurX >= smallWidth){blurX = smallWidth - 1;}
                if(blurY >= smallHeight){blurY = smallHeight - 1;}

                int blurRGB = blur[blurX+blurY*smallWidth];

                int originalR = (originalRGB >> 16) & 0xFF;
                int originalG = (originalRGB >> 8) & 0xFF;
                int originalB = originalRGB & 0xFF;;

                int blurR = (blurRGB >> 16) & 0xFF;
                int blurG = (blurRGB >> 8) & 0xFF;
                int blurB = blurRGB & 0xFF;

                int finalR = Math.min(255, originalR + blurR);
                int finalG = Math.min(255, originalG + blurG);
                int finalB = Math.min(255, originalB + blurB);

                int finalRGB =
                        (255 << 24) |
                                (finalR << 16) |
                                (finalG << 8) |
                                finalB;

                finalPixels[x + y * image.getWidth()] = finalRGB;
            }
        }

        BufferedImage finalImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int[] outputPixels = ((DataBufferInt)
                finalImage.getRaster().getDataBuffer()
        ).getData();
        System.arraycopy(finalPixels, 0, outputPixels, 0, finalPixels.length);
        return finalImage;
    }
}
