package lib.postProcessEffects;

import lib.PostProcessEffect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Bloom2 extends PostProcessEffect {

    BufferedImage brightBuffer;
    BufferedImage smallBuffer;
    BufferedImage blurBuffer;
    BufferedImage upscaleBuffer;
    BufferedImage finalBuffer;

    float threshold = 0.3f;
    float reduction = 4f;

    private void ensureBuffersExist(BufferedImage image) {
        if (
                brightBuffer == null ||
                        brightBuffer.getWidth() != image.getWidth() ||
                        brightBuffer.getHeight() != image.getHeight()
        ) {

            brightBuffer = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
        }
        if (
                smallBuffer == null ||
                        smallBuffer.getWidth() != Math.ceil(image.getWidth()/reduction) ||
                        smallBuffer.getHeight() != Math.ceil(image.getHeight()/reduction)
        ) {

            smallBuffer = new BufferedImage(
                    (int)Math.ceil(image.getWidth()/reduction),
                    (int)Math.ceil(image.getHeight()/reduction),
                    BufferedImage.TYPE_INT_ARGB
            );
        }
        if (
                upscaleBuffer == null ||
                        upscaleBuffer.getWidth() != image.getWidth() ||
                        upscaleBuffer.getHeight() != image.getHeight()
        ) {

            upscaleBuffer = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
        }
        if (
                finalBuffer == null ||
                        finalBuffer.getWidth() != image.getWidth() ||
                        finalBuffer.getHeight() != image.getHeight()
        ) {

            finalBuffer = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
        }
    }

    public BufferedImage extractBrightAreas(BufferedImage image, BufferedImage save){
        int[] brightPass = new int[image.getHeight() * image.getWidth()];

        int[] imagePixels = ((DataBufferInt)
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

        int[] outputPixels = ((DataBufferInt)
                save.getRaster().getDataBuffer()
        ).getData();
        System.arraycopy(brightPass, 0, outputPixels, 0, brightPass.length);

        return save;
    }

    public void blur(BufferedImage image, BufferedImage save){
        Graphics2D sbG = smallBuffer.createGraphics();
        sbG.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        sbG.setComposite(AlphaComposite.Clear);
        sbG.fillRect(0, 0, smallBuffer.getWidth(), smallBuffer.getHeight());
        sbG.setComposite(AlphaComposite.SrcOver);

        sbG.drawImage(image, 0, 0, smallBuffer.getWidth(), smallBuffer.getHeight(), null);

        Graphics2D g = save.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, save.getWidth(), save.getHeight());
        g.setComposite(AlphaComposite.SrcOver);

        g.drawImage(smallBuffer, 0, 0, save.getWidth(), save.getHeight(), null);
    }

    public void combine(BufferedImage image, BufferedImage save){
        int[] imagePixels = ((DataBufferInt)
                image.getRaster().getDataBuffer()
        ).getData();

        int[] blur = ((DataBufferInt)(image.getRaster().getDataBuffer())).getData();

        int[] finalPixels = new int[image.getWidth() * image.getHeight()];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int originalRGB = imagePixels[x + y * image.getWidth()];

                int blurRGB = blur[x + y * image.getWidth()];

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

        int[] outputPixels = ((DataBufferInt)
                save.getRaster().getDataBuffer()
        ).getData();
        System.arraycopy(finalPixels, 0, outputPixels, 0, finalPixels.length);
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        ensureBuffersExist(image);
        extractBrightAreas(image, brightBuffer);
        blur(brightBuffer, upscaleBuffer);
        combine(upscaleBuffer, finalBuffer);
        return upscaleBuffer;
    }
}
