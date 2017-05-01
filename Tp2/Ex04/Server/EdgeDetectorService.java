package Tp2.Ex04.Server;

import Tp2.Ex04.Common.IEdgeDetectorService;
import Tp2.Ex04.Common.ImageChunkHandler;
import Tp2.Ex04.Common.ImageSerializable;
import Tp2.Ex04.Common.SobelEdgeDetector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * User: juan
 * Date: 27/04/17
 * Time: 19:53
 */
public class EdgeDetectorService implements IEdgeDetectorService{

    // Will split image in 4 parts: 2 rows and 2 cols
    public static final int IMAGE_ROWS = 2;
    public static final int IMAGE_COLS = 2;
    public static final int REDUNDANT_PIXELS = 1;

    private SobelEdgeDetector sobelEdgeDetector;
    private String dns;

    public String getDns() {return dns;}
    public void setId(String id) {this.dns = id;}


    public EdgeDetectorService(){
        this.sobelEdgeDetector = new SobelEdgeDetector();
    }

    @Override
    public int[][] detectEdges(int[][] pixelsValues) throws RemoteException {
        this.log("In detect edges to image: " + pixelsValues.length + "x" + pixelsValues[0].length);
        return sobelEdgeDetector.getPixelValuesEdged(pixelsValues);
    }

    public ImageSerializable detectEdges(ImageSerializable image){
        int[][] pixelsValues = this.sobelEdgeDetector.getPixelValuesEdged(image.getBufferedImage());
        return new ImageSerializable(this.sobelEdgeDetector.toBufferedImage(pixelsValues));
    }

    public void log(String message){
        System.out.println(this.dns + ": " + message);
    }
}
