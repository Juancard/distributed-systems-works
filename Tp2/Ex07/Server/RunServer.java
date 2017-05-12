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
        this.prepareBackupServer();
        this.prepareMainServers();
        this.prepareLoadBalancer();
    }

    private void prepareBackupServer() throws IOException {
        int port = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
        String filesPath = properties.getProperty("BACKUP_FILES_PATH");
        String logFilePath = properties.getProperty("BACKUP_LOG_FILE_PATH");

        BackupServer backupServer = new BackupServer(port, filesPath, logFilePath);
        this.backupServer = new Thread(backupServer);
    }

    private void prepareMainServers() throws IOException {
        int numberOfMainServers = 1;
        this.mainServersThreads = new Thread[numberOfMainServers];

        // Main server data
        String serverHost = properties.getProperty("SERVER_HOST");
        int serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        String filesPath = properties.getProperty("SERVER_FILES_PATH");
        String logFilePath = properties.getProperty("SERVER_LOG_FILE_PATH");
        ServerInfo serverInfo = new ServerInfo(serverHost, serverPort);
        this.mainServersInfo = new ServerInfo[]{serverInfo};

        // Backup server data
        String backupHost = properties.getProperty("BACKUP_SERVER_HOST");
        int backupPort = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
        ServerInfo backupServerInfo = new ServerInfo(backupHost, backupPort);

        // Database data
        String databaseUrl = properties.getProperty("DB_URL");

        for (int i=0; i < numberOfMainServers; i++){
            ServerInfo s = this.mainServersInfo[i];
            MainServer mainServer = new MainServer(
                    s.getPort(),
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
