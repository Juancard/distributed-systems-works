package Tp2.Ex04;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * User: juan
 * Date: 26/04/17
 * Time: 19:38
 */
public class ImageChunkHandler {
    private final int cells;
    private final int rows;
    private final int cols;
    private final int redundantPixel;

    public ImageChunkHandler(int rows, int cols, int redundantPixel) {
        this.rows = rows;
        this.cols = cols;
        this.redundantPixel = redundantPixel;
        this.cells = rows * cols;
    }

    public BufferedImage[] split(BufferedImage toSplit){
        BufferedImage[] chunks = new BufferedImage[cells];
        int originalWidth = toSplit.getWidth();
        int originalHeight = toSplit.getHeight();
        int chunkWidth = originalWidth / this.cols + redundantPixel;
        int chunkHeight = originalHeight / this.rows + redundantPixel;

        int chunkCounter = 0;
        for (int x=0; x < this.rows; x++){
            for (int y = 0; y < this.cols; y++){
                BufferedImage newChunk = new BufferedImage(chunkWidth, chunkHeight, toSplit.getType());
                newChunk.createGraphics().drawImage(
                        toSplit,
                        0, 0,
                        chunkWidth, chunkHeight,
                        chunkWidth * y, chunkHeight * x,
                        chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight,
                        null
                );
                chunks[chunkCounter] = newChunk;
                chunkCounter++;
            }
        }
        /*
        BufferedImage image1 = new BufferedImage(CHUNK_WIDTH, CHUNK_HEIGHT, originalImage.getType());
        image1.createGraphics().drawImage(originalImage, 0, 0, CHUNK_WIDTH, CHUNK_HEIGHT, 0, 0, CHUNK_WIDTH, CHUNK_HEIGHT, null);

        BufferedImage image2 = new BufferedImage(CHUNK_WIDTH, CHUNK_HEIGHT, originalImage.getType());
        image2.createGraphics().drawImage(originalImage, 0, 0, CHUNK_WIDTH, CHUNK_HEIGHT, width / 2 - REDUNDANT_PIXELS, 0, width, height, null);
                           */
        return chunks;
    }

    public BufferedImage join(BufferedImage[] toJoin){
        int chunkWidth = toJoin[0].getWidth() - this.redundantPixel;
        int chunkHeight = toJoin[0].getHeight() - this.redundantPixel;
        int type = toJoin[0].getType();

        BufferedImage joined = new BufferedImage(chunkWidth * this.cols, chunkHeight * this.rows, type);

        int cell = 0;
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.cols; y++) {
                BufferedImage thisChunk = toJoin[cell];
                joined.createGraphics().drawImage(
                        thisChunk,
                        chunkWidth * y, chunkHeight * x,
                        chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight,
                        0, 0,
                        chunkWidth,  chunkHeight,
                        null
                );
                cell++;
            }
        }

        return joined;
        /*
        // Remove redundant pixel
        BufferedImage finalImage1 = new BufferedImage(CHUNK_WIDTH - REDUNDANT_PIXELS, height, image1.getType());
        finalImage1.createGraphics().drawImage(image1, 0, 0, CHUNK_WIDTH - REDUNDANT_PIXELS, height, 0, 0, CHUNK_WIDTH - REDUNDANT_PIXELS, height, null);

        // Remove redundant pixel
        BufferedImage finalImage2 = new BufferedImage(CHUNK_WIDTH - REDUNDANT_PIXELS, height, image2.getType());
        finalImage2.createGraphics().drawImage(image2, 0, 0, CHUNK_WIDTH - REDUNDANT_PIXELS, height, REDUNDANT_PIXELS, 0, CHUNK_WIDTH, height, null);

        // Construct originalImage with chunk images
        BufferedImage finalImg = new BufferedImage(width, height, image1.getType());
        finalImg.createGraphics().drawImage(finalImage1, 0, 0, null);
        finalImg.createGraphics().drawImage(finalImage2, width / 2, 0, null);
        */
    }

}
