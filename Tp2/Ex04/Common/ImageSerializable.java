package Tp2.Ex04.Common;

import java.awt.image.BufferedImage;
import java.io.*;

/**
 * User: juan
 * Date: 27/04/17
 * Time: 20:18
 */
public class ImageSerializable implements Serializable{

    private int width;
    private int height;
    private int type;
    private int[] data;

    public ImageSerializable(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.type = image.getType();
        this.data =  this.setArrayImage(image);//image.getRGB(0, 0, this.width, this.height, null, 0, this.width);
    }

    public BufferedImage getBufferedImage(){
        BufferedImage bi = new BufferedImage(this.width, this.height, this.type);

        int pos = 0;
        for (int x=0; x < this.width; x++){
            for (int y = 0; y < this.height; y++){
                bi.setRGB(x, y, this.data[pos]);
                pos++;
            }
        }

        return bi;
    }

    public int[] setArrayImage(BufferedImage bi) {
        int out[] = new int[this.width * this.height];
        int pos = 0;
        for (int x=0; x < this.width; x++){
            for (int y = 0; y < this.height; y++){
                out[pos] = bi.getRGB(x, y);
                pos++;
            }
        }
        return out;
    }

}
