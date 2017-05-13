package Tp2.Ex04.Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * User: juan
 * Date: 27/04/17
 * Time: 19:41
 */
public interface IEdgeDetectorService extends Remote {
    public static final String DNS_NAME = "EDGE_DETECTOR_SERVICE";

    public ImageSerializable detectEdges(ImageSerializable image) throws RemoteException;
    public int[][] detectEdges(int[][] imagePixels) throws RemoteException;

}
