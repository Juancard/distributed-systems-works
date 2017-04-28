package Tp2.Ex04.Client;

import Tp2.Ex04.Common.IEdgeDetectorService;
import Tp2.Ex04.Common.ImageSerializable;

import java.awt.image.BufferedImage;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * User: juan
 * Date: 27/04/17
 * Time: 19:57
 */
public class EdgeDetectorClient implements IEdgeDetectorService {

    private IEdgeDetectorService iEdgeDetectorService;

    public EdgeDetectorClient(String host, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);
        this.iEdgeDetectorService = (IEdgeDetectorService) registry.lookup(IEdgeDetectorService.DNS_NAME);
    }

    @Override
    public ImageSerializable detectEdges(ImageSerializable image) throws RemoteException {
        return this.iEdgeDetectorService.detectEdges(image);
    }
    public BufferedImage detectEdges(BufferedImage image) throws RemoteException {
        return this.iEdgeDetectorService.detectEdges(new ImageSerializable(image)).getBufferedImage();
    }
}
