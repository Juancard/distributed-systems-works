package Tp2.Ex04.Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * User: juan
 * Date: 27/04/17
 * Time: 19:41
 */
public interface IEdgeDetectorService extends Remote {

    public int[][] detectEdges(int[][] imagePixels) throws RemoteException;

}
