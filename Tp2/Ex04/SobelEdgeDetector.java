package Tp2.Ex04;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: juan
 * Date: 25/04/17
 * Time: 21:38
 */
public class SobelEdgeDetector {

    public static final int[][] KERNEL_GX = new int[][]{
            { -1, 0, 1 },
            { -2, 0, 2 },
            { -1, 0, 1 }
    };

    public static final int[][] KERNEL_GY = new int[][]{
            { -1, -2, -1 },
            { 0, 0, 0 },
            { 1, 2, 1 }
    };

    public static final int KERNEL_SIZE = 3;

    private int maxPixelValue;
    public int getMaxPixelValue(){return maxPixelValue;}

    public SobelEdgeDetector(){
        this.maxPixelValue = 0;
    }

    public BufferedImage getImageEdged(BufferedImage imageInput){
        int[][] pixels = this.getPixelValuesEdged(imageInput);
        pixels = this.normalize(pixels, this.maxPixelValue);
        return this.fillWithPixels(imageInput, pixels);
    }

    public int[][] getPixelValuesEdged(BufferedImage imageInput){

        this.toGreyScale(imageInput);

        // White background for .png images
        imageInput = this.toWhiteBackground(imageInput);

        int[][] pixels = this.toPixelArray2d(imageInput);

        return this.processPixelValues(pixels);
    }

    private int[][] processPixelValues(int[][] input) {
        int width = input.length;
        int height = input[0].length;
        int[][] output = new int[width][height];

        int accumulatorGX, accumulatorGY;
        int neighborX, neighborY, valueImage, valueKernelGX, valueKernelGY, sobelValue;

        for (int imageX = 0; imageX < width; imageX++) {
            for (int imageY = 0; imageY < height; imageY++) {
                //System.out.println(String.format("Pixel: (%d, %d) = %d", imageY, imageX, input[imageX][imageY]));
                if (imageX == 0 || imageY == 0 || imageX == width - 1 || imageY == height - 1){
                    sobelValue = 0;
                } else {
                    accumulatorGX = 0;
                    accumulatorGY = 0;

                    for (int kernelX = 0; kernelX < KERNEL_SIZE; kernelX++){
                        for (int kernelY = 0; kernelY < KERNEL_SIZE; kernelY++){
                            neighborX = imageX + -1 + kernelX;
                            neighborY = imageY + -1 + kernelY;

                            valueImage = input[neighborX][neighborY];

                            valueKernelGX = KERNEL_GX[kernelX][kernelY];
                            valueKernelGY = KERNEL_GY[kernelX][kernelY];

                            accumulatorGX += valueImage * valueKernelGX;
                            accumulatorGY += valueImage * valueKernelGY;
                        }
                    }
                    sobelValue = this.combineGxWithGy(accumulatorGX, accumulatorGY);
                    if (sobelValue > this.maxPixelValue) this.maxPixelValue = sobelValue;
                }
                output[imageX][imageY] = sobelValue;
            }
        }

        return output;
    }

    private int combineGxWithGy(int accumulatorGX, int accumulatorGY) {
        double GX2 = Math.pow(accumulatorGX, 2);
        double GY2 = Math.pow(accumulatorGY, 2);
        return (int) Math.sqrt(GX2 + GY2);
    }

    public int[][] normalize(int[][] pixels) {
        return this.normalize(pixels, this.getMaxPixelValue(pixels));
    }

    public int[][] normalize(int[][] pixels, int maxPixelValue) {
        float ratio = (float) maxPixelValue / 255;
        System.out.println(this.getMaxPixelValue(pixels) + " " + ratio);
        return this.normalize(pixels, ratio);
    }

    private int getMaxPixelValue(int[][] pixels) {
        int max = 0;

        for (int i=0; i < pixels.length; i++)
            for (int j=0; j < pixels[i].length; j++)
                if (pixels[i][j] > max) max = pixels[i][j];

        return max;
    }

    private int[][] normalize(int[][] pixels, float ratio) {
        for (int i=0; i < pixels.length; i++){
            for (int j=0; j < pixels[i].length; j++){
                pixels[i][j] = (int)(pixels[i][j] / ratio);
            }
        }
        return pixels;
    }

    public BufferedImage fillWithPixels(BufferedImage image, int[][] pixels) {
        for( int i = 0; i < image.getWidth(); i++ )
            for( int j = 0; j < image.getHeight(); j++ )
                image.setRGB(i, j, this.toRgbColor(pixels[i][j]));

        return image;
    }

    private int toPixelValue(int rgbColor){
        // blue, green and red have same value if image is black and white
        // Returns blue, but could be any of the three mentioned above
        return new Color(rgbColor).getBlue();
    }
    private int toRgbColor(int pixelValue){
        return (pixelValue<<16 | pixelValue<<8 | pixelValue);
    }


    public int[][] toPixelArray2d(BufferedImage image) {
        int[][] pixels = new int[image.getWidth()][image.getHeight()];
        for( int i = 0; i < image.getWidth(); i++ )
            for( int j = 0; j < image.getHeight(); j++ )
                pixels[i][j] = this.toPixelValue(image.getRGB(i, j));

        return pixels;
    }

    private BufferedImage toWhiteBackground(BufferedImage image) {
        BufferedImage newBufferedImage = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);

        return newBufferedImage;
    }

    private void toGreyScale(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int rgbColor, alpha, red, green, blue, avg, newRgbColor;
        for (int x=0; x < width; x++) {
            for (int y=0; y < height; y++) {
                rgbColor = image.getRGB(x, y);
                alpha = (rgbColor>>24)&0xff;
                red = (rgbColor>>16)&0xff;
                green = (rgbColor>>8)&0xff;
                blue = rgbColor&0xff;

                avg = (red + green + blue)/3;

                newRgbColor = (alpha<<24) | (avg<<16) | (avg<<8) | avg;
                image.setRGB(x, y, newRgbColor);
            }
        }
    }

}
