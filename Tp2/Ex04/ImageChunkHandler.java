package Tp2.Ex04;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: juan
 * Date: 26/04/17
 * Time: 19:38
 */
public class ImageChunkHandler {
    private final int totalChunks;
    private final int rows;
    private final int cols;
    private final int redundantPixel;

    public ImageChunkHandler(int rows, int cols, int redundantPixel) {
        this.rows = rows;
        this.cols = cols;
        this.redundantPixel = redundantPixel;
        this.totalChunks = rows * cols;
    }

    public BufferedImage[] split(BufferedImage toSplit){
        BufferedImage[] chunks = new BufferedImage[totalChunks];
        int originalWidth = toSplit.getWidth();
        int originalHeight = toSplit.getHeight();
        int chunkWidth = originalWidth / this.rows + redundantPixel;
        int chunkHeight = originalHeight / this.cols + redundantPixel;

        int chunckCounter = 0;
        for (int x=0; x < this.rows; x++){
            for (int y = 0; y < this.cols; y++){
                BufferedImage newChunk = new BufferedImage(chunkWidth, chunkHeight, toSplit.getType());
                newChunk.createGraphics().drawImage(
                        toSplit,
                        0,
                        0,
                        chunkWidth,
                        chunkHeight,
                        chunkWidth * y,
                        chunkHeight * x,
                        chunkWidth * y + chunkWidth,
                        chunkHeight * x + chunkHeight,
                        null
                );
                chunks[chunckCounter] = newChunk;
                chunckCounter++;
            }
        }

        return chunks;
    }

    public BufferedImage join(BufferedImage[] toJoin){
        int chunkWidth = toJoin[0].getWidth() - this.redundantPixel;
        int chunkHeight = toJoin[0].getHeight() - this.redundantPixel;
        int type = toJoin[0].getType();

        BufferedImage joined = new BufferedImage(chunkWidth * this.rows, chunkHeight * this.cols, type);

        int chunkCounter = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                joined.createGraphics().drawImage(
                        toJoin[chunkCounter],
                        chunkWidth * j,
                        chunkHeight * i,
                        null
                );
                chunkCounter++;
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
