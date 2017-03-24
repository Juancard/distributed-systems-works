package Tp1.Ex06.Server;


import Tp1.Ex06.Common.IVectorService;

import java.rmi.RemoteException;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:37
 */

public class VectorService implements IVectorService {

    public VectorService() {

    }

    @Override
    public int[] add(int[] vector1, int[] vector2) throws RemoteException {
        return new int[]{1, 2};
    }

    @Override
    public int[] substract(int[] vector1, int[] vector2) throws RemoteException {
        return new int[]{2, 1};
    }
}
