package Tp2.Ex04.Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * User: juan
 * Date: 27/04/17
 * Time: 19:41
 */
public interface IEdgeDetectorService extends Remote {
    public static final String DNS_NAME_1 = "EDGE_DETECTOR_SERVICE_1";
    public static final String DNS_NAME_2 = "EDGE_DETECTOR_SERVICE_2";
    public static final String DNS_NAME_3 = "EDGE_DETECTOR_SERVICE_3";
    public static final String DNS_NAME_4 = "EDGE_DETECTOR_SERVICE_4";

    public ImageSerializable detectEdges(ImageSerializable image) throws RemoteException;
    public int[][] detectEdges(int[][] imagePixels) throws RemoteException;

}
