package Tp1.Ex06.Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:23
 */
public interface IVectorService extends Remote {
    public static final String DNS_NAME = "VECTOR_SERVICE";

    public int[] add(int[] vector1, int[] vector2) throws RemoteException;
    public int[] substract(int[] vector1, int[] vector2) throws RemoteException;
}
