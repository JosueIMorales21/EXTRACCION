package ConfigProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final String CONFIG_FILE = "..\\config.properties"; // Ruta del archivo config.properties

    private Properties properties;

    public ConfigReader() {
        properties = new Properties();
        try ( FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
        }
    }

    public String getDatabaseUrl() {
        return properties.getProperty("db.url");
    }

    public String getDatabaseUser() {
        return properties.getProperty("db.user");
    }

    public String getDatabasePassword() {
        return properties.getProperty("db.password");
    }

    public String getTableName() {
        return properties.getProperty("table.name");
    }

    public String getZipPassword() {
        return properties.getProperty("zip.password");
    }
}
