package Tp2.Ex07.Server;

import Common.PropertiesManager;
import Common.ServerInfo;
import Tp2.Ex01.Server.BackupServer.BackupServer;
import Tp2.Ex07.Server.LoadBalancer.LoadBalancer;
import Tp2.Ex07.Server.MainServer.MainServer;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 12/05/17
 * Time: 15:26
 */
public class RunServer {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex07/config.properties";

    private Properties properties;
    private Thread backupServer;
    private Thread loadBalancerServer;
    private ServerInfo[] mainServersInfo;
    private Thread[] mainServersThreads;


    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        RunServer runServer = new RunServer(properties);
        runServer.start();
    }

    public RunServer(Properties properties) throws IOException {
        this.properties = properties;
        this.mainServersInfo = this.loadMainServersInfo();
        this.prepareBackupServer();
        this.prepareMainServers();
        this.prepareLoadBalancer();
    }

    private ServerInfo[] loadMainServersInfo() {
        final String SERVER_SEPARATOR = ",";
        final String HOST_PORT_SEPARATOR = ":";

        String serversValue = this.properties.getProperty("MAIN_SERVERS");
        String[] pairsHostPort = serversValue.split(SERVER_SEPARATOR);
        ServerInfo[] out = new ServerInfo[pairsHostPort.length];

        for (int i=0; i<pairsHostPort.length; i++) {
            String[] hostPost = pairsHostPort[i].split(HOST_PORT_SEPARATOR);
            out[i] = new ServerInfo(hostPost[0], Integer.parseInt(hostPost[1]));
        }

        return out;
    }

    private void prepareBackupServer() throws IOException {
        int port = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
        String filesPath = properties.getProperty("BACKUP_FILES_PATH");
        String logFilePath = properties.getProperty("BACKUP_LOG_FILE_PATH");

        BackupServer backupServer = new BackupServer(port, filesPath, logFilePath);
        this.backupServer = new Thread(backupServer);
    }

    private void prepareMainServers() throws IOException {
        int numberOfMainServers = this.mainServersInfo.length;
        this.mainServersThreads = new Thread[numberOfMainServers];

        // Main server common data;
        String filesPath = properties.getProperty("SERVER_FILES_PATH");
        String logFilePath = properties.getProperty("SERVER_LOG_FILE_PATH");

        // Backup server data
        String backupHost = properties.getProperty("BACKUP_SERVER_HOST");
        int backupPort = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
        ServerInfo backupServerInfo = new ServerInfo(backupHost, backupPort);

        // Database data
        String databaseUrl = properties.getProperty("DB_URL");

        for (int i=0; i < numberOfMainServers; i++){
            MainServer mainServer = new MainServer(
                    this.mainServersInfo[i].getPort(),
                    backupServerInfo,
                    databaseUrl,
                    filesPath,
                    logFilePath
            );
            this.mainServersThreads[i] = new Thread(mainServer);
        }
    }

    private void prepareLoadBalancer() {
        int port = Integer.parseInt(properties.getProperty("BALANCER_PORT"));

        LoadBalancer loadBalancer = new LoadBalancer(port);
        for (ServerInfo mainServer : this.mainServersInfo)
            loadBalancer.addServer(mainServer);

        this.loadBalancerServer = new Thread(loadBalancer);
    }

    private void start() {
        this.backupServer.start();
        this.loadBalancerServer.start();
        for (Thread t : this.mainServersThreads)
            t.start();
    }
}
