package Tp2.Ex05.Server;

import Common.LogManager;
import Tp2.Ex05.Common.IEdgeDetectorService;
import Tp2.Ex05.Common.SobelEdgeDetector;

import java.rmi.RemoteException;

/**
 * User: juan
 * Date: 27/04/17
 * Time: 19:53
 */
public class EdgeDetectorService implements IEdgeDetectorService{

    private SobelEdgeDetector sobelEdgeDetector;
    private String id;
    private LogManager logManager;

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}


    public EdgeDetectorService(){
        this.sobelEdgeDetector = new SobelEdgeDetector();
    }
    public EdgeDetectorService(String id, LogManager logManager){
        this.id = id;
        this.logManager = logManager;
        this.sobelEdgeDetector = new SobelEdgeDetector();
    }

    @Override
    public int[][] detectEdges(int[][] pixelsValues) throws RemoteException {
        this.log("In detect edges to image: " + pixelsValues.length + "x" + pixelsValues[0].length);
        return sobelEdgeDetector.getPixelValuesEdged(pixelsValues);
    }

    public void log(String message){
        this.logManager.log(this.id + ": " + message);
    }

    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }
}
