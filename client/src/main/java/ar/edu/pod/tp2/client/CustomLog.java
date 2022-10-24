package ar.edu.pod.tp2.client;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CustomLog {
    private static final CustomLog INSTANCE = new CustomLog();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm:ss:SSS")
            .withZone(ZoneId.systemDefault());

    private CustomLog() {
    }

    public static CustomLog GetInstance() {
        return INSTANCE;
    }

    /**
     * Given a filename and a message, it outputs a log with time and the message
     * @param filename Path to the file
     * @param message Message to be written
     * @param append Determines if the file is created or appended to
     */
    public void writeTimestamp(String method,
                               String className, int line, String filename, String message, boolean append) {
        try {
            FileWriter myWriter = new FileWriter(filename, append);
            myWriter.write(FORMATTER.format(Instant.now()) + " INFO " + className + ":" + line + " - " + message + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred when writing log to " + filename);
        }
    }

    /**
     * Given a filename and a message, it outputs a message
     * @param filename Path to the file
     * @param message Message to be written
     */
    public void write(String filename, String message) {
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(message);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred when writing log to " + filename);
        }
    }
}
