package lib.postProcessEffects;

import lib.PostProcessEffect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Bloom extends PostProcessEffect {

    public int radius = 3;
    public float threshold = 0.1f;
    public int reduction = 8;

    BufferedImage originalResizeImage;
    int[] originalResizeImagePixels;
    BufferedImage resizeImage;
    int[] resizeImagePixels;
    Graphics2D resizeGraphics;
    public int[] resize(int[] image, int orgW, int orgH, int dstW, int dstH){
        if(originalResizeImage == null || originalResizeImage.getWidth() != orgW || originalResizeImage.getHeight() != orgH){
            originalResizeImage = new BufferedImage(orgW, orgH, BufferedImage.TYPE_INT_ARGB);
            originalResizeImagePixels = ((DataBufferInt)(originalResizeImage.getRaster().getDataBuffer())).getData();
        }
        if(resizeImage == null || resizeImage.getWidth() != dstW || resizeImage.getHeight() != dstH){
            resizeImage = new BufferedImage(dstW, dstH, BufferedImage.TYPE_INT_ARGB);
            resizeImagePixels = ((DataBufferInt)(resizeImage.getRaster().getDataBuffer())).getData();
            resizeGraphics = resizeImage.createGraphics();
            resizeGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
        System.arraycopy(image, 0, originalResizeImagePixels, 0, image.length);

        resizeGraphics.setComposite(AlphaComposite.Clear);
        resizeGraphics.fillRect(0,0,dstW, dstH);
        resizeGraphics.setComposite(AlphaComposite.SrcOver);

        resizeGraphics.drawImage(originalResizeImage, 0, 0, dstW, dstH, null);

        return resizeImagePixels;
    }

    public int[] brightPass(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        int[] brightPass = new int[width*height];
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

        return brightPass;
    }

    public int[] blur(int[] image, int width, int height){
        int[] temp = new int[width*height];
        int[] blurPixels = new int[width*height];

        // horizontal pass
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int totalR = 0;
                int totalG = 0;
                int totalB = 0;
                int samples = 0;
                for (int i = -radius; i <= radius; i++) {
                    int sx = Math.max(Math.min(width-1, x+i), 0);
                    int sample = image[sx+y*width];

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

                temp[x+y*width] =(255 << 24) |
                        (totalR << 16) |
                        (totalG << 8) |
                        totalB;
            }
        }

        // vertical pass
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int totalR = 0;
                int totalG = 0;
                int totalB = 0;
                int samples = 0;
                for (int i = -radius; i <= radius; i++) {
                    int sy = Math.max(Math.min(height-1, y+i), 0);
                    int sample = temp[x+sy*width];

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

                blurPixels[x+y*width] =
                        (255 << 24) |
                                (totalR << 16) |
                                (totalG << 8) |
                                totalB;
            }
        }

        return blurPixels;
    }

    BufferedImage finalOutput;
    public BufferedImage combine(int[] image1, int[] image2, int width, int height){
        if(finalOutput == null || finalOutput.getWidth() != width || finalOutput.getHeight() != height){
            finalOutput = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        int[] finalImage = new int[width*height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = image1[x+y*width];
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = rgb1 & 0xff;

                int rgb2 = image2[x+y*width];
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = rgb2 & 0xff;

                int r = Math.max(Math.min(r1+r2, 255), 0);
                int g = Math.max(Math.min(g1+g2, 255), 0);
                int b = Math.max(Math.min(b1+b2, 255), 0);

                int rgb =
                        (255 << 24) |
                                (r << 16) |
                                (g << 8) |
                                b;
                finalImage[x+y*width] = rgb;
            }
        }
        int[] outputPixels = ((DataBufferInt)(finalOutput.getRaster().getDataBuffer())).getData();
        System.arraycopy(finalImage, 0, outputPixels, 0, finalImage.length);

        return finalOutput;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        int smallW = image.getWidth()/reduction;
        int smallH = image.getHeight()/reduction;
        int width = image.getWidth();
        int height = image.getHeight();
        return combine(
                ((DataBufferInt)(image.getRaster().getDataBuffer())).getData(),
                resize(
                        blur(resize(
                                brightPass(image),
                                width,
                                height,
                                smallW,
                                smallH
                        ), smallW, smallH),
                        smallW,
                        smallH,
                        width,
                        height
                ),
                width,
                height
        );
    }
}
