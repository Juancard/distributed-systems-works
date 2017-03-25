package Tp1.Ex08.Client;

import Tp1.Ex08.Common.ITask;
import Tp1.Ex08.Common.ITaskService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 15:00
 */
public class TaskClient {

    private ITaskService iTaskService;

    public TaskClient(String host, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);
        this.iTaskService = (ITaskService) registry.lookup(ITaskService.DNS_NAME);
    }

    public Object executeInServer(ITask object) throws RemoteException {
        return this.iTaskService.execute(object);
    }
}
