package Common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * User: juan
 * Date: 08/05/17
 * Time: 14:28
 */
public class PropertiesManager {
    public static Properties loadProperties(String propertiesPath) throws IOException {
        Properties defaultProps = new Properties();
        FileInputStream in = new FileInputStream(propertiesPath);
        defaultProps.load(in);
        in.close();
        return defaultProps;
    }
}
