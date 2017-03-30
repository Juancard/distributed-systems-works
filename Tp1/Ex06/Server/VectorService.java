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
        int size = Math.min(vector1.length, vector2.length);
        int[] out = new int[size];
        for (int i=0; i<size; i++)
            out[i] = vector1[i] + vector2[i];
        
        // To test that RMI parameters are passed by value
        vector1 = this.modifyingBug(vector1);
        vector2 = this.modifyingBug(vector2);
        
        return out;
    }

    @Override
    public int[] substract(int[] vector1, int[] vector2) throws RemoteException {
        int size = Math.min(vector1.length, vector2.length);
        int[] out = new int[size];
        for (int i=0; i<size; i++)
            out[i] = vector1[i] - vector2[i];
        
        // To test that RMI parameters are passed by value
        vector1 = this.modifyingBug(vector1);
        vector2 = this.modifyingBug(vector2);        
        
        return out;
    }
    
    // To test that RMI parameters are passed by value
    private int[] modifyingBug(int[] toBeModified){
    	int[] out = new int[toBeModified.length];
    	for (int i=0; i<toBeModified.length; i++){
    		out[i] = -1;
    	}
    	return out;
    }
}
