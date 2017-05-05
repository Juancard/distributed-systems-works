package Tp2.Ex05.Server;

import Tp2.Ex05.Common.IEdgeDetectorService;
import Tp2.Ex05.Common.ImageSerializable;
import Tp2.Ex05.Common.SobelEdgeDetector;

import java.rmi.RemoteException;

/**
 * User: juan
 * Date: 27/04/17
 * Time: 19:53
 */
public class EdgeDetectorService implements IEdgeDetectorService{

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
