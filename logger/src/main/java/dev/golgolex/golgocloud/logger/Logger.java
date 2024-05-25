/*
 * Copyright (c) Tarek Hosni El Alaoui 2017
 */

package dev.golgolex.golgocloud.logger;

import jline.console.ConsoleReader;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;

/**
 * Logger is a custom implementation of java.util.logging.Logger that provides additional functionalities
 * for logging and interacting with the console.
 */
@Getter
@Setter
public class Logger extends java.util.logging.Logger {
    private final String lineSeparator = System.lineSeparator();
    private final String systemUserName = System.getProperty("user.name");
    private final List<Printer> logPrinters = new LinkedList<>();
    private final ConsoleReader consoleReader;
    private boolean debugging = false;
    private boolean showPrompt = !Boolean.getBoolean("logging.prompt.disabled");

    /**
     * Constructs a new instance of CloudLogger.
     *
     * @param directory The directory where log files will be stored.
     * @throws IOException            If an I/O error occurs.
     * @throws NoSuchFieldException   If a specified field does not exist.
     * @throws IllegalAccessException If access to a field is denied.
     */
    public Logger(@NotNull File directory)
            throws IOException,
            NoSuchFieldException,
            IllegalAccessException {
        // Call the constructor of the superclass.
        super("CloudLogger", null);

        // Create necessary directories if they do not exist.
        if (!Files.exists(Paths.get(directory.getPath() + "/" + "local"))) {
            Files.createDirectory(Paths.get(directory.getPath() + "/" + "local"));
        }
        if (!Files.exists(Paths.get(directory.getPath() + "/" + "local", "logs"))) {
            Files.createDirectory(Paths.get(directory.getPath() + "/" + "local", "logs"));
        }

        // Set the logging level to capture all levels of logging.
        this.setLevel(Level.ALL);

        // Initialize console reader for interactive input.
        this.consoleReader = new ConsoleReader(System.in, System.out);
        this.consoleReader.setExpandEvents(false);

        // Set up file logging with rotation and formatting.
        var fileHandler = new FileHandler(directory.getPath() + "/local/logs/cloud.log", 8000000, 8, true);
        fileHandler.setEncoding(StandardCharsets.UTF_8.name());
        fileHandler.setFormatter(new LogFormatting());
        this.addHandler(fileHandler);

        // Configure console reader.
        this.consoleReader.setPrompt("");
        this.consoleReader.resetPromptLine("", "", 0);

        // Set up logging handler for system output and error streams.
        var loggingHandler = new PublishHandler(
                this.logPrinters,
                this.consoleReader,
                false
        );

        var formatter = new LogFormatting();
        loggingHandler.setFormatter(formatter);
        loggingHandler.setEncoding(StandardCharsets.UTF_8.name());
        loggingHandler.setLevel(Level.INFO);
        this.addHandler(loggingHandler);

        // Redirect standard output and error streams to logging streams.
        System.setOut(new AsyncPrintStream(new OutputStream(
                this.lineSeparator,
                Level.INFO,
                this
        )));
        System.setErr(new AsyncPrintStream(new OutputStream(
                this.lineSeparator,
                Level.SEVERE,
                this
        )));
    }

    /**
     * Writes a debug message to the logger if debugging is enabled.
     *
     * @param message The debug message to be written.
     */
    public void writeDebug(@NotNull String message) {
        // Check if debugging is enabled.
        if (this.debugging) {
            // Log the debug message with level WARNING.
            log(Level.WARNING, "[DEBUG] " + message);
        }
    }

    /**
     * Reads a line of text from the console with the specified prompt.
     *
     * @param prompt The prompt to be displayed to the user.
     * @return The line of text read from the console, or null if an IOException occurs.
     */
    public String readLine(@NotNull String prompt) {
        try {
            // Read a line of text from the console.
            var line = this.consoleReader.readLine(this.showPrompt ? prompt : null);
            // Set the prompt for the next input line.
            this.consoleReader.setPrompt(" ");
            return line;
        } catch (IOException e) {
            // Print the error message if an IOException occurs.
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Shuts down all handlers associated with the logger and closes the reader.
     */
    public void shutdownAll() {
        // Close all handlers associated with the logger.
        for (var handler : getHandlers()) {
            handler.close();
        }
        try {
            // Clear the current line in the reader.
            this.consoleReader.killLine();
        } catch (IOException e) {
            // Print the error message if an IOException occurs.
            System.err.println(e.getMessage());
        }
    }
}
