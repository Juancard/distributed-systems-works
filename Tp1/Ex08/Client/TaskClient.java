package Tp1.Ex08.Client;

import Tp1.Ex08.Common.ITask;
import Tp1.Ex08.Common.ITaskService;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public static final String POLICY_PATH = "/Tp1/Ex08/Common/task.policy";
    private ITaskService iTaskService;

    public TaskClient(String host, int port) throws RemoteException, NotBoundException {
        this.setSecurity();
        Registry registry = LocateRegistry.getRegistry(host, port);
        this.iTaskService = (ITaskService) registry.lookup(ITaskService.DNS_NAME);
    }

    private void setSecurity() {
        System.setProperty("java.security.policy", loadPolicy().getPath());
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
    }

    private File loadPolicy() {
        InputStream in = this.getClass().getResourceAsStream(POLICY_PATH);
        try {
            File tempFile = File.createTempFile("policy", ".tmp");
            tempFile.deleteOnExit();
            IOUtils.copy(in, new FileOutputStream(tempFile));
            return tempFile;
        } catch (IOException e) {
            System.out.println("Error loading client policy: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }


    public Object executeInServer(ITask object) throws RemoteException {
        return this.iTaskService.execute(object);
    }
}
