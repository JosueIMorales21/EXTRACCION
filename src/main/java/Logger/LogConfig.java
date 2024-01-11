package Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogConfig {

    public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void configureLogger() {
        try {
            // Obtén la fecha actual para incluirla en el nombre del archivo de registro
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String currentDate = dateFormat.format(new Date());
            String logFileName = "log" + currentDate + ".log";

            String logFilePath = "..\\logger\\" + logFileName;

            // Configurar el formato del logger
            FileHandler fileHandler = new FileHandler(logFilePath);
            fileHandler.setFormatter(new MyCustomFormatter()); // Configura el formatter personalizado
            logger.addHandler(fileHandler);

            // Establecer el nivel de registro (puedes ajustarlo según tus necesidades)
            logger.setLevel(Level.ALL);

            // Log de inicio
            logger.log(Level.INFO, "Logger configurado con éxito.");

        } catch (IOException e) {
            // Log de excepción en caso de error en la configuración
            logger.log(Level.SEVERE, "Error al configurar el logger", e);
        }
    }
}
