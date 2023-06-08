package logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class for logging exceptions
 */
public class Logger {
    private FileWriter writer;


    public Logger() throws IOException {
        try {
            File logfile = new File("log.txt");
            writer = new FileWriter(logfile);
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом log.txt");
        }
    }

    /**
     * Write exception to log.txt
     *
     * @param e Exception to write
     */
    public void write(Exception e) throws IOException {
        try {
            var s = e.getStackTrace();
            for (StackTraceElement stackTraceElement : s) {
                writer.write(stackTraceElement.toString() + "\n");
            }
        } catch (IOException ex) {
            System.out.println("Ошибка при записи ошибки в log.txt");
        }
    }

    public void close() throws IOException {
        try {
            writer.flush();
            writer.close();
        } catch (IOException io){
            System.out.println("Ошибка при записи ошибки в log.txt");
        }
    }
    /**
     * Overload of write to write errors
     *
     * @param e Error to write
     */
    public void write(Error e) throws IOException {
        try {
            var s = e.getStackTrace();
            for (StackTraceElement stackTraceElement : s) {
                writer.write(stackTraceElement.toString() + "\n");
            }
            writer.write("\n");
        } catch (IOException io) {
            System.out.println("Проблемы с записью в log.txt");
        }
    }
}
