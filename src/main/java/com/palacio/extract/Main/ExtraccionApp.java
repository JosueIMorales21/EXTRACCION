package com.palacio.extract.Main;

import CSVCompressor.Compressor;
import ConfigProperties.ConfigReader;
import Logger.LogConfig;
import static Logger.LogConfig.logger;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class ExtraccionApp {

    public static void main(String[] args) {
        // Configuración del logger
        LogConfig.configureLogger();

        ConfigReader configReader = new ConfigReader();
        String jdbcUrl = configReader.getDatabaseUrl();
        String user = configReader.getDatabaseUser();
        String password = configReader.getDatabasePassword();
        String tableName = configReader.getTableName();
        String zipPassword = configReader.getZipPassword();

        // Configuración de la conexión a la base de datos (SQLite)
        try {
            // Cargar dinámicamente el controlador JDBC de SQLite
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establecer la conexión con la base de datos (SQLite)
            try ( Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
                // Puedes ejecutar consultas directas ahora
                // Por ejemplo, selecciona todas las columnas de la tabla
                String sqlQuery = "SELECT * FROM " + tableName;

                // Obtener el directorio actual de trabajo
                String directorioActual = System.getProperty("user.dir");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDate = dateFormat.format(new Date());

                try ( PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);  ResultSet resultSet = preparedStatement.executeQuery();  PrintWriter csvWriter = new PrintWriter(new FileWriter(directorioActual + "\\CONSULTA_" + currentDate + "_" + tableName + ".csv"))) {

                    // Obtener información sobre las columnas
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // Imprimir encabezados de columna
                    for (int i = 1; i <= columnCount; i++) {
                        csvWriter.print(metaData.getColumnName(i));
                        if (i < columnCount) {
                            csvWriter.print(",");
                        }
                    }
                    csvWriter.println();  // Nueva línea después de los encabezados

                    // Imprimir datos de la tabla
                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            csvWriter.print(resultSet.getString(i));
                            if (i < columnCount) {
                                csvWriter.print(",");
                            }
                        }
                        csvWriter.println();  // Nueva línea después de cada fila
                    }

                    // Log para indicar que la operación fue exitosa
                    logger.log(Level.INFO, "Consulta y escritura en el archivo CSV completadas con éxito.");

                } catch (IOException e) {
                    // Log de excepciones de escritura de archivo
                    logger.log(Level.SEVERE, "Error al escribir en el archivo CSV", e);
                }

            }
        } catch (ClassNotFoundException | SQLException e) {
            // Log de excepciones de base de datos
            logger.log(Level.SEVERE, "Error de base de datos", e);
            e.printStackTrace();
        }

        Compressor.compressCSVFilesToZIP(".\\", zipPassword);

        Compressor.deleteAllCSVsInDirectory(".\\");

    }

}
