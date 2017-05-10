package Tp2.Ex07.Server.MainServer;

import Common.PropertiesManager;

import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 14/04/17
 * Time: 17:38
 */
public class RunMainServer extends Tp2.Ex01.Server.MainServer.RunMainServer{

    public static final String PROPERTIES_PATH = "distributed-systems-works/Tp2/Ex07/config.properties";
    private String databaseUrl;

    public RunMainServer(Properties properties) throws IOException {
        super(properties);
        this.databaseUrl = properties.getProperty("DB_URL");
    }

    protected void run() throws IOException {
        MainServer mainServer = new MainServer(
                this.serverPort,
                this.backupServerInfo,
                this.databaseUrl,
                this.filesPath,
                this.logFilePath
        );
        mainServer.startServer();
    }

    public static void main(String[] args) {

        try {
            Properties properties = PropertiesManager.loadProperties(RunMainServer.PROPERTIES_PATH);
            RunMainServer runMainServer= new RunMainServer(properties);
            runMainServer.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
