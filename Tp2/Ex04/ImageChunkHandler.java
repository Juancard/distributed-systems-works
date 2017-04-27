package Tp2.Ex04;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * User: juan
 * Date: 26/04/17
 * Time: 19:38
 */
public class ImageChunkHandler {
    private int cells;
    private int rows;
    private int cols;
    private int redundantPixel;
    private BufferedImage originalImage;

    public ImageChunkHandler(int rows, int cols, int redundantPixel) {
        this.init(rows, cols, redundantPixel, null);
    }

    public ImageChunkHandler(int rows, int cols, int redundantPixel, BufferedImage originalImage) {
        this.init(rows, cols, redundantPixel, originalImage);
    }

    private void init(int rows, int cols, int redundantPixel, BufferedImage originalImage) {
        this.rows = rows;
        this.cols = cols;
        this.redundantPixel = redundantPixel;
        this.cells = rows * cols;
        this.originalImage = originalImage;
    }

    public BufferedImage[] split() throws IOException {
        if (this.originalImage == null)
            throw new IOException("No Image specified");

        return this.split(this.originalImage);
    }

    public BufferedImage[] split(BufferedImage toSplit){
        BufferedImage[] chunks = new BufferedImage[cells];

        int chunkWidth = toSplit.getWidth() / this.cols;
        int chunkHeight = toSplit.getHeight() / this.rows;
        int cell = 0;

        int dstx1 = 0, dsty1 = 0;
        int dstx2, dsty2, srcx1, srcy1, srcx2, srcy2;
        for (int x=0; x < this.rows; x++){
            for (int y = 0; y < this.cols; y++){

                dstx2 = chunkWidth;
                dsty2 = chunkHeight;
                srcx1 = chunkWidth * y;
                srcx2 = srcx1 + chunkWidth;
                srcy1 = chunkHeight * x;
                srcy2 = srcy1 + chunkHeight;

                if (y != 0) {
                    dstx2 += this.redundantPixel; // adds redundant pixels
                    srcx1 -= this.redundantPixel; //add redundant pixels from left side of image
                }
                if (y != this.cols - 1) {
                    dstx2 += this.redundantPixel; // adds redundant pixels
                    srcx2 += this.redundantPixel; //add redundant pixels from right side of image
                }
                if (x != 0) {
                    dsty2 += this.redundantPixel; // adds redundant pixels
                    srcy1 -= this.redundantPixel; //add redundant pixels from up side of image
                }
                if (x != this.rows - 1) {
                    dsty2 += this.redundantPixel; // adds redundant pixels
                    srcy2 += this.redundantPixel; //add redundant pixels from bottom side of image
                }

                BufferedImage newChunk = new BufferedImage(dstx2, dsty2, toSplit.getType());
                newChunk.createGraphics().drawImage(
                        toSplit,
                        dstx1, dsty1,
                        dstx2, dsty2,
                        srcx1, srcy1,
                        srcx2, srcy2,
                        null
                );
                chunks[cell] = newChunk;
                cell++;
            }
        }

        return chunks;
    }

    public BufferedImage join(BufferedImage[] toJoin){
        int originalWidth;
        int originalHeight;

        if (this.originalImage == null){
            originalWidth = this.getImageWidthFromChunks(toJoin);
            originalHeight = this.getImageHeightFromChunks(toJoin);
        } else {
            originalHeight = this.originalImage.getHeight();
            originalWidth = this.originalImage.getWidth();
        }

        int type = toJoin[0].getType();
        BufferedImage joined = new BufferedImage(originalWidth, originalHeight, type);

        int chunkWidth = originalWidth / this.cols;
        int chunkHeight = originalHeight / this.rows;

        int cell = 0;
        int dstx1, dsty1;
        int dstx2, dsty2, srcx1, srcy1, srcx2, srcy2;
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.cols; y++) {
                BufferedImage thisChunk = toJoin[cell];

                dstx1 = chunkWidth * y;
                dstx2 = dstx1 + chunkWidth;

                dsty1 = chunkHeight * x;
                dsty2 = dsty1 + chunkHeight;

                srcx1 = 0;
                srcx2 = chunkWidth;

                srcy1 = 0;
                srcy2 = chunkHeight;

                if (y != 0) {
                    srcx2 += this.redundantPixel;
                    srcx1 += this.redundantPixel;
                }
                if (x != 0) {
                    srcy2 += this.redundantPixel;
                    srcy1 += this.redundantPixel;
                }
                joined.createGraphics().drawImage(
                        thisChunk,
                        dstx1, dsty1,
                        dstx2, dsty2,
                        srcx1, srcy1,
                        srcx2,  srcy2,
                        null
                );

                cell++;
            }
        }

        return joined;
    }

    private int getImageWidthFromChunks(BufferedImage[] chunks) {
        int redundantPixelsOnWidth = 2 * this.redundantPixel * (this.cols - 1);
        int allWidthPixels = 0;
        for (int col=0; col < this.cols; col++) {
            allWidthPixels += chunks[col].getWidth();
        }
        return allWidthPixels - redundantPixelsOnWidth;
    }

    private int getImageHeightFromChunks(BufferedImage[] chunks) {
        int redundantPixelsOnHeight = 2 * this.redundantPixel * (this.rows - 1);
        int allHeightPixels = 0;
        for (int row=0; row < this.rows; row++) {
            allHeightPixels += chunks[row].getHeight();
        }
        return allHeightPixels - redundantPixelsOnHeight;
    }


    public int getCells() {
        return cells;
    }

    public void setCells(int cells) {
        this.cells = cells;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRedundantPixel() {
        return redundantPixel;
    }

    public void setRedundantPixel(int redundantPixel) {
        this.redundantPixel = redundantPixel;
    }


    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(BufferedImage originalImage) {
        this.originalImage = originalImage;
    }
}
