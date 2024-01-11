package CSVCompressor;

import static Logger.LogConfig.logger;
import java.io.File;
import java.util.logging.Level;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class Compressor {

    public static void compressCSVFilesToZIP(String inputDir, String zipPassword) {
        try {
            // Obtener la lista de archivos CSV en el directorio de entrada
            File[] csvFiles = new File(inputDir).listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

            if (csvFiles != null && csvFiles.length > 0) {
                // Obtener el directorio actual de trabajo
                String currentDir = System.getProperty("user.dir");

                // Comprimir todos los archivos CSV a un solo archivo ZIP en el directorio actual
                compressFilesToZIP(csvFiles, currentDir, zipPassword);

                logger.info("Proceso de compresión completado con éxito.");
            } else {
                logger.info("No hay archivos CSV en el directorio de entrada.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error durante el proceso de compresión.", e);
        }
    }

    private static void compressFilesToZIP(File[] files, String outputDir, String zipPassword) {
        try {
            net.lingala.zip4j.model.ZipParameters parameters = new net.lingala.zip4j.model.ZipParameters();
            parameters.setCompressionMethod(CompressionMethod.DEFLATE);
            parameters.setCompressionLevel(CompressionLevel.NORMAL);
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);

            // Obtener el nombre base del primer archivo CSV
            String baseFileName = files[0].getName().replaceFirst("[.][^.]+$", "");

            // Construir el nombre del archivo ZIP
            String zipFileName = baseFileName + ".zip";
            File zipFile = new File(outputDir, zipFileName);

            // Crear el archivo ZIP
            net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(zipFile);
            zip.setPassword(zipPassword.toCharArray());

            // Agregar cada archivo CSV al archivo ZIP
            for (File file : files) {
                // Añadir el archivo al ZIP con el mismo nombre pero en la raíz del ZIP
                zip.addFile(file, parameters);
            }

            logger.info("Archivos CSV comprimidos con éxito en: " + zipFile.getAbsolutePath());
        } catch (net.lingala.zip4j.exception.ZipException e) {
            // Manejo de Excepciones
            logger.log(Level.SEVERE, "Error al comprimir los archivos CSV", e);
        }
    }

    public static void deleteAllCSVsInDirectory(String directoryPath) {
        try {
            File directory = new File(directoryPath);

            // Verificar si el directorio existe
            if (!directory.exists() || !directory.isDirectory()) {
                logger.severe("El directorio no existe o no es válido.");
                return;
            }

            // Obtener la lista de archivos CSV en el directorio
            File[] csvFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

            if (csvFiles != null && csvFiles.length > 0) {
                for (File csvFile : csvFiles) {
                    if (!csvFile.delete()) {
                        logger.warning("No se pudo eliminar el archivo CSV: " + csvFile.getName());
                    }
                }

                logger.info("Archivos CSV eliminados con éxito en: " + directoryPath);
            } else {
                logger.info("No hay archivos CSV en el directorio.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error durante el proceso de eliminación de archivos CSV", e);
        }
    }
}
