package fileIO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String LOG_FILE = "log.txt";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd HH:mm:ss yyyy");

    /**
     * log the events that occurs when the game lasts (with the round info, round:)
     * 
     */
    public static void log(int round, String message) {
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = dtf.format(LocalDateTime.now());
            writer.write("[" + timestamp + "] Round " + round + ": " + message);
            writer.newLine();
        }
        catch (IOException e) {
            System.err.println( e.getMessage());
        }
    }

    /**
     * log the general events without the round info like starting of the game and finish of the game.
     * 
     */
    public static void logGeneral(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = dtf.format(LocalDateTime.now());
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
        } 
        catch (IOException e) {
            System.err.println( e.getMessage());
        }
    }
}