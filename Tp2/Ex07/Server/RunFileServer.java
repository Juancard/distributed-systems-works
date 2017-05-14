package Tp2.Ex07.Server;

import Common.CommonMain;
import Common.FileManager;
import Common.PropertiesManager;
import Common.ServerInfo;
import Tp2.Ex01.Server.BackupServer.BackupServer;
import Tp2.Ex07.Server.LoadBalancer.LoadBalancer;
import Tp2.Ex07.Server.MainServer.Database.DatabaseManager;
import Tp2.Ex07.Server.MainServer.Database.ScriptRunner;
import Tp2.Ex07.Server.MainServer.MainServer;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * User: juan
 * Date: 12/05/17
 * Time: 15:26
 */
public class RunFileServer {

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex07/config.properties";

    private Properties properties;
    private Thread backupServer;
    private Thread loadBalancerServer;
    private ServerInfo[] mainServersInfo;
    private Thread[] mainServersThreads;
    private String databaseUrl;


    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesManager.loadProperties(PROPERTIES_PATH);
        CommonMain.showWelcomeMessage(properties);
        RunFileServer runFileServer = new RunFileServer(properties);
        runFileServer.start();
    }

    public RunFileServer(Properties properties) throws IOException {
        this.properties = properties;
        this.mainServersInfo = this.loadMainServersInfo();
        this.prepareBackupServer();
        this.prepareDatabase();
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
        CommonMain.createSection("Preparing Backup Server");

        int port = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
        CommonMain.display("This server will be listening on port: " + port);

        String filesPathValue = properties.getProperty("BACKUP_FILES_PATH");
        File filesPath = FileManager.loadFilesPath(filesPathValue);

        BackupServer backupServer = new BackupServer(port, filesPath);

        String logFilePath = properties.getProperty("BACKUP_LOG_PATH");
        if (new File(logFilePath).isDirectory())
            logFilePath = new File(logFilePath).toString() + "/backup_server.log";

        backupServer.setLogWriter(
                this.createLogWriter(logFilePath)
        );

        this.backupServer = new Thread(backupServer);
    }

    private void prepareDatabase() throws IOException {
        /*
            Sets up database:
            - Checks if given db url is a valid database url.
            - If database is not found in path:
                - Creates it.
                - fill it with data from script.
            - Checks connection.

            Finally: set attribute database url.
         */
        CommonMain.createSection("Preparing Database");

        // Database data
        String dbUrl = properties.getProperty("DB_URL");

        if (!dbUrl.matches("^jdbc:.*:.*"))
            throw new IOException("Not a valid database URL: " + dbUrl);

        String dbName = dbUrl.split("^jdbc:.*:")[1];
        File dbNameFile = new File(dbName);
        if (! (dbNameFile.exists())){
            CommonMain.display("Database was not found. Creating it...");
            Connection connection;
            try {
                connection = new DatabaseManager(dbUrl).getConnection();
            } catch (SQLException e) {
                String m = "Could not connect to database. Cause: " + e.getMessage();
                throw new IOException(m);
            }
            ScriptRunner scriptRunner = new ScriptRunner(connection, false, true);
            scriptRunner.setLogWriter(null);
            String scriptPath = this.properties.getProperty("DB_SCRIPT");
            CommonMain.display("Filling database with script: " + scriptPath);
            BufferedReader script = new BufferedReader(new FileReader(new File(scriptPath)));
            try {
                scriptRunner.runScript(script);
            } catch (SQLException e) {
                throw new IOException("Could not load db data from script. Cause: " + e.getMessage());
            }
        }

        CommonMain.display("Database is ready.");
        this.databaseUrl = dbUrl;
    }

    private void prepareMainServers() throws IOException {
        CommonMain.createSection("Preparing Main Servers");

        int numberOfMainServers = this.mainServersInfo.length;
        this.mainServersThreads = new Thread[numberOfMainServers];

        // Main server common data;
        String filesPathValue = properties.getProperty("SERVER_FILES_PATH");
        File filesPath = FileManager.loadFilesPath(filesPathValue);
        String logFilePath = properties.getProperty("SERVER_LOG_PATH");
        File log = new File(logFilePath);

        // Backup server data
        String backupHost = properties.getProperty("BACKUP_SERVER_HOST");
        int backupPort = Integer.parseInt(properties.getProperty("BACKUP_SERVER_PORT"));
        ServerInfo backupServerInfo = new ServerInfo(backupHost, backupPort);

        int port;
        String mainServerLog;
        for (int i=0; i < numberOfMainServers; i++){
            port = this.mainServersInfo[i].getPort();
            CommonMain.display("Main server on port: " + port);

            MainServer mainServer = new MainServer(
                    port,
                    backupServerInfo,
                    this.databaseUrl,
                    filesPath
            );

            mainServerLog = log.toString();
            if (log.isDirectory())
                mainServerLog += "/main_server_" + port + ".log";

            mainServer.setLogWriter(
                    this.createLogWriter(mainServerLog)
            );

            this.mainServersThreads[i] = new Thread(mainServer);
        }
    }

    private void prepareLoadBalancer() throws IOException {
        CommonMain.createSection("Preparing Load Balancer");

        int port = Integer.parseInt(properties.getProperty("BALANCER_PORT"));
        CommonMain.display("This server will listen on port: " + port);
        LoadBalancer loadBalancer = new LoadBalancer(port);

        String logPath = properties.getProperty("BALANCER_LOG_PATH");
        if (new File(logPath).isDirectory())
            logPath = new File(logPath).toString() + "/load_balancer.log";
        loadBalancer.setLogWriter(this.createLogWriter(logPath));

        for (ServerInfo mainServer : this.mainServersInfo)
            loadBalancer.addServer(mainServer);

        this.loadBalancerServer = new Thread(loadBalancer);
    }

    private void start() {
        CommonMain.createSection("Starting server");

        this.backupServer.start();
        this.loadBalancerServer.start();
        for (Thread t : this.mainServersThreads)
            t.start();

        CommonMain.display("Server is up and running!");
    }

    private PrintStream createLogWriter(String logFilePath) throws IOException {
        File logFile = new File(logFilePath);
        if (! (logFile.exists())){
            CommonMain.display("Log file was not found. Creating it...");
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                throw new IOException("Could not create log file. Cause: " + e.getMessage());
            }
        }
        CommonMain.display("Logs in: " + logFile.getPath());

        return new PrintStream(
                new FileOutputStream(
                        logFile,
                        true
                )
        );
    }
}
