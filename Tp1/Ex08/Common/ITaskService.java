package Tp1.Ex08.Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:23
 */
public interface ITaskService extends Remote {
    public static final String DNS_NAME = "TASK_SERVICE";

    public Object execute(ITask object) throws RemoteException;
}
