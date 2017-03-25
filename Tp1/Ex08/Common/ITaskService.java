package Tp1.Ex08.Common;

import Tp1.Ex03.Common.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:23
 */
public interface ITaskService extends Remote {
    public static final String DNS_NAME = "TASK_SERVICE";

    public Object execute(ITask object) throws RemoteException;
}
