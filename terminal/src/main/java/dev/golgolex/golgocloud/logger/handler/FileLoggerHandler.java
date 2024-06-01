package dev.golgolex.golgocloud.logger.handler;

import dev.golgolex.golgocloud.logger.LogLevel;
import dev.golgolex.golgocloud.logger.LoggerHandler;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public final class FileLoggerHandler implements LoggerHandler {

    private static final SimpleDateFormat LOG_LAYOUT = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat FILE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");

    private static final Pattern COLOR_PATTERN = Pattern.compile("&[0-9a-fA-Fk-oK-OrR]");

    private final BufferedWriter logWriter;
    private static final Path LOGS_PATH = Path.of("local/logs");
    private static final Path LATEST_LOG = LOGS_PATH.resolve("latest.log");

    @SneakyThrows
    public FileLoggerHandler() {
        if (!Files.exists(LOGS_PATH)) {
            Files.createDirectories(LOGS_PATH);
        }
        this.logWriter = new BufferedWriter(new FileWriter(LATEST_LOG.toString(), true));
    }

    @Override
    @SneakyThrows
    public void print(LogLevel level, String message, Throwable throwable, Object... objects) {
        logWriter.append("[").append(LOG_LAYOUT.format(Calendar.getInstance().getTime())).append("]: ").append(removeColorCodes(message)).append("\n");
        logWriter.flush();
    }

    @Override
    @SneakyThrows
    public void close() {

        this.logWriter.close();
        if (!java.nio.file.Files.exists(LATEST_LOG)) {
            return;
        }

        var timelines = Long.parseLong(System.getProperty("startup"));
        var date = new Date(timelines);

        LATEST_LOG.toFile().renameTo(LOGS_PATH.resolve(FILE_FORMAT.format(date) + ".log").toFile().getAbsoluteFile());
        //todo no exception print
    }

    private String removeColorCodes(String message) {
        return COLOR_PATTERN.matcher(message).replaceAll("");
    }

}
