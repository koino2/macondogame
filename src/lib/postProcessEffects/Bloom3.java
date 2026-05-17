package lib.postProcessEffects;

import lib.PostProcessEffect;
import lib.spam;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Bloom3 extends PostProcessEffect {
    float threshold = 0.1f;

    int reduction = 4;

    int radius = 12;

    BufferedImage image;
    int width;
    int height;

    int[] brightPass;
    BufferedImage brightPassImage;

    BufferedImage smallImage;
    int smallWidth, smallHeight;

    BufferedImage blurImage;

    BufferedImage upscale;

    BufferedImage finalOutput;

    public void ensureBuffersExist(){
        width = image.getWidth();
        height = image.getHeight();
        if(brightPass == null || brightPass.length != width * height){
            brightPass = new int[width * height];
        }
        if(brightPassImage == null || brightPassImage.getWidth() != width || brightPassImage.getHeight() != height){
            brightPassImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        if(finalOutput == null || finalOutput.getWidth() != width || finalOutput.getHeight() != height){
            finalOutput = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        if(smallImage == null || smallImage.getWidth() != width/reduction || smallImage.getHeight() != height/reduction){
            smallImage = new BufferedImage(width/reduction, height/reduction, BufferedImage.TYPE_INT_ARGB);
        }
        if(blurImage == null || blurImage.getWidth() != width/reduction || blurImage.getHeight() != height/reduction){
            blurImage = new BufferedImage(width/reduction, height/reduction, BufferedImage.TYPE_INT_ARGB);
        }
        if(upscale == null || upscale.getWidth() != width || upscale.getHeight() != height){
            upscale = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        smallWidth = width/reduction;
        smallHeight = height/reduction;
    }

    public BufferedImage brightPass(){
        int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int RGB = imagePixels[x+y*width];
                int r = (RGB >> 16) & 0xFF;
                int g = (RGB >> 8) & 0xFF;
                int b = RGB & 0xFF;

                int brightness = (r+g+b)/3;
                if(brightness > threshold*255){
                    brightPass[x+y*width] = RGB;
                } else{
                    brightPass[x+y*width] = 0;
                }
            }
        }

        int[] brightPixels = ((DataBufferInt)(brightPassImage.getRaster().getDataBuffer())).getData();
        System.arraycopy(brightPass, 0, brightPixels, 0, brightPass.length);
        return brightPassImage;
    }

    public int[] downscale(BufferedImage image){
        Graphics2D g = smallImage.createGraphics();
        g.drawImage(image, 0, 0, smallImage.getWidth(), smallImage.getHeight(), null);

        int[] smallPixels = ((DataBufferInt)(smallImage.getRaster().getDataBuffer())).getData();
        return smallPixels;
    }

    public BufferedImage blur(int[] image){
        int[] temp = new int[smallWidth*smallHeight];
        int[] blurPixels = new int[smallWidth*smallHeight];

        // horizontal pass
        for (int y = 0; y < smallHeight; y++) {
            for (int x = 0; x < smallWidth; x++) {
                int totalR = 0;
                int totalG = 0;
                int totalB = 0;
                int samples = 0;
                for (int i = -radius; i <= radius; i++) {
                    int sx = Math.max(Math.min(smallWidth-1, x+i), 0);
                    int sample = image[sx+y*smallWidth];

                    int r = (sample >> 16) & 0xff;
                    int g = (sample >> 8) & 0xff;
                    int b = sample & 0xff;

                    totalR += r;
                    totalB += b;
                    totalG += g;
                    samples++;
                }
                totalR /= samples;
                totalG /= samples;
                totalB /= samples;

                temp[x+y*smallWidth] =(255 << 24) |
                        (totalR << 16) |
                        (totalG << 8) |
                        totalB;
            }
        }

        // vertical pass
        for (int y = 0; y < smallHeight; y++) {
            for (int x = 0; x < smallWidth; x++) {
                int totalR = 0;
                int totalG = 0;
                int totalB = 0;
                int samples = 0;
                for (int i = -radius; i <= radius; i++) {
                    int sy = Math.max(Math.min(smallHeight-1, y+i), 0);
                    int sample = temp[x+sy*smallWidth];

                    int r = (sample >> 16) & 0xff;
                    int g = (sample >> 8) & 0xff;
                    int b = sample & 0xff;

                    totalR += r;
                    totalB += b;
                    totalG += g;
                    samples++;
                }
                totalR /= samples;
                totalG /= samples;
                totalB /= samples;

                blurPixels[x+y*smallWidth] =
                        (totalR << 16) |
                                (totalG << 8) |
                                totalB;
            }
        }

        int[] output = ((DataBufferInt)(blurImage.getRaster().getDataBuffer())).getData();
        System.arraycopy(blurPixels, 0, output, 0, blurPixels.length);
        return blurImage;
    }

    public BufferedImage upscale(BufferedImage image) {
        Graphics2D g = upscale.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        return upscale;
    }

    public BufferedImage combine(BufferedImage image1, BufferedImage image2){
        int[] image1pixels = ((DataBufferInt)(image1.getRaster().getDataBuffer())).getData();
        int[] image2pixels = ((DataBufferInt)(image2.getRaster().getDataBuffer())).getData();

        int width = image1.getWidth();
        int height = image2.getHeight();

        int[] finalImage = new int[width*height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = image1pixels[x+y*width];
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = rgb1 & 0xff;

                int rgb2 = image2pixels[x+y*width];
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = rgb2 & 0xff;

                int r = Math.max(Math.min(r1+r2, 255), 0);
                int g = Math.max(Math.min(g1+g2, 255), 0);
                int b = Math.max(Math.min(b1+b2, 255), 0);

                int rgb = (r << 16) | (g << 8) | b;
                finalImage[x+y*width] = rgb;
            }
        }
        int[] outputPixels = ((DataBufferInt)(finalOutput.getRaster().getDataBuffer())).getData();
        System.arraycopy(finalImage, 0, outputPixels, 0, finalImage.length);

        return finalOutput;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        this.image = image;
        ensureBuffersExist();
        return combine(image, upscale(blur(downscale(brightPass()))));
    }
}
