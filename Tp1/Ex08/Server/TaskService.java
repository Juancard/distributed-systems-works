package Tp1.Ex08.Server;

import Tp1.Ex08.Common.ITask;
import Tp1.Ex08.Common.ITaskService;

import java.rmi.RemoteException;

/**
 * User: juan
 * Date: 20/03/17
 * Time: 14:37
 */

public class TaskService implements ITaskService {
    @Override
    public Object execute(ITask object) throws RemoteException {
        return object.execute();
    }
}
